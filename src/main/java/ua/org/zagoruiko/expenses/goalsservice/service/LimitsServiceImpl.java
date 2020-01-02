package ua.org.zagoruiko.expenses.goalsservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.org.zagoruiko.expenses.goalsservice.dao.LimitsDao;
import ua.org.zagoruiko.expenses.goalsservice.dto.LimitDTO;
import ua.org.zagoruiko.expenses.goalsservice.dto.LimitReportItemDTO;
import ua.org.zagoruiko.expenses.goalsservice.model.Limit;
import ua.org.zagoruiko.expenses.goalsservice.model.LimitReportItem;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LimitsServiceImpl implements LimitsService {
    private final static int DEFAULT_MONTHLY_LIMIT = 75000;

    private LimitsDao limitsDao;

    @Autowired
    public LimitsServiceImpl(LimitsDao limitsDao) {
        this.limitsDao = limitsDao;
    }

    private Limit fromDTO(LimitDTO limitDTO) {
        if (limitDTO == null) {
            return null;
        }
        return new Limit(
                limitDTO.getYear(),
                limitDTO.getMonth(),
                limitDTO.getCategory(),
                limitDTO.getFamily(),
                limitDTO.getLimit()
        );
    }

    private LimitDTO fromModel(Limit limit) {
        if (limit == null) {
            return null;
        }
        return new LimitDTO(
                limit.getYear(),
                limit.getMonth(),
                limit.getCategory(),
                limit.getFamily(),
                limit.getLimit()
        );
    }

    private LimitReportItem fromDTO(LimitReportItemDTO limitDTO) {
        if (limitDTO == null) {
            return null;
        }
        return new LimitReportItem(
                limitDTO.getYear(),
                limitDTO.getMonth(),
                limitDTO.getCategory(),
                limitDTO.getAmount(),
                limitDTO.getLimit(),
                limitDTO.getPercent()
        );
    }

    private LimitReportItemDTO fromModel(LimitReportItem limit) {
        if (limit == null) {
            return null;
        }
        return new LimitReportItemDTO(
                limit.getYear(),
                limit.getMonth(),
                limit.getCategory(),
                limit.getAmount(),
                limit.getLimit(),
                limit.getPercent()
        );
    }

    @Override
    public LimitDTO setLimit(LimitDTO limit) {
        Limit limitModel = this.limitsDao.setLimit(fromDTO(limit));
        return fromModel(limitModel);
    }

    @Override
    public LimitDTO setMonthlyLimit(LimitDTO limit) {
        Limit limitModel = this.limitsDao.setMonthlyLimit(fromDTO(limit));
        return fromModel(limitModel);
    }

    @Override
    public LimitDTO getLimit(int year, int month, String category, String family) {
        return fromModel(this.limitsDao.getLimit(year, month, category, family));
    }

    @Override
    public List<LimitDTO> getLimits(int year, int month, String family) {
        return this.limitsDao.getLimits(year, month, family)
                .stream().map(this::fromModel).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<LimitReportItemDTO> getLimitReport(int year, int month, String family) {
        List<LimitReportItemDTO> limits = this.limitsDao.getLimitReport(year, month, family)
                .stream().map(this::fromModel).collect(Collectors.toList());
        if (!limits.isEmpty()) {
            return limits;
        }
        int oldYear = year;
        int oldMonth = month - 1;
        if (oldMonth == 0) {
            oldYear--;
            oldMonth = 12;
        }
        List<LimitReportItemDTO> oldLimits = this.getLimitReport(oldYear, oldMonth, family);
        if (oldLimits.isEmpty()) {
            return limits;
        }
        renewLimits(oldLimits, family);
        return getLimitReport(year, month, family);
    }

    @Override
    public void renewLimits(List<LimitReportItemDTO> oldLimits, String family) {
        List<LimitDTO> newLimits = new ArrayList<>();
        for (LimitReportItemDTO limit: oldLimits) {
            int diff = limit.getLimit() - limit.getAmount();
            if (diff > 0) {
                diff = diff / 2;
            }
            int newLimitAmount = limit.getLimit() + diff;
            newLimitAmount = Math.max(limit.getLimit() / 2, newLimitAmount);
            newLimits.add(new LimitDTO(
                    limit.getMonth() == 12 ? limit.getYear() + 1 : limit.getYear(),
                    limit.getMonth() == 12 ? 1 : limit.getMonth() + 1,
                    limit.getCategory(),
                    family,
                    newLimitAmount
            ));
            newLimits.forEach(l -> this.setLimit(l));
        }
    }

    @Override
    @Transactional
    public LimitReportItemDTO getMonthlyLimitStatus(int year, int month, String family) {
        LimitReportItemDTO limit =
                fromModel(this.limitsDao.getMonthlyLimitStatus(year, month, family));
        if (limit != null) {
            return limit;
        }
        int oldYear = year;
        int oldMonth = month - 1;
        if (oldMonth == 0) {
            oldYear--;
            oldMonth = 12;
        }
        LimitReportItemDTO oldLimit =
                fromModel(this.limitsDao.getMonthlyLimitStatus(oldYear, oldMonth, family));
        if (oldLimit == null) {
            LimitDTO newLimit = new LimitDTO(year, month, "", family, DEFAULT_MONTHLY_LIMIT);
            this.setMonthlyLimit(newLimit);
            return fromModel(this.limitsDao.getMonthlyLimitStatus(year, month, family));
        }
        renewMonthlyLimit(oldLimit, family);
        return getMonthlyLimitStatus(year, month, family);
    }

    @Override
    public void renewMonthlyLimit(LimitReportItemDTO oldLimit, String family) {
        int diff = oldLimit.getLimit() - oldLimit.getAmount();
        if (diff > 0) {
            diff = diff / 2;
        }
        int newLimitAmount = oldLimit.getLimit() + diff;

        newLimitAmount = Math.max(oldLimit.getLimit() / 2, newLimitAmount);

        LimitDTO newLimit = new LimitDTO(
                oldLimit.getMonth() == 12 ? oldLimit.getYear() + 1 : oldLimit.getYear(),
                oldLimit.getMonth() == 12 ? 1 : oldLimit.getMonth() + 1,
                "",
                family,
                newLimitAmount
        );
        this.setMonthlyLimit(newLimit);
    }
}
