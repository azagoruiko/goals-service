package ua.org.zagoruiko.expenses.goalsservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.org.zagoruiko.expenses.goalsservice.dao.LimitsDao;
import ua.org.zagoruiko.expenses.goalsservice.dto.LimitDTO;
import ua.org.zagoruiko.expenses.goalsservice.model.Limit;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LimitsServiceImpl implements LimitsService {
    private LimitsDao limitsDao;

    @Autowired
    public LimitsServiceImpl(LimitsDao limitsDao) {
        this.limitsDao = limitsDao;
    }

    private Limit fromDTO(LimitDTO limitDTO) {
        return new Limit(
                limitDTO.getYear(),
                limitDTO.getMonth(),
                limitDTO.getCategory(),
                limitDTO.getFamily(),
                limitDTO.getLimit()
        );
    }

    private LimitDTO fromModel(Limit limit) {
        return new LimitDTO(
                limit.getYear(),
                limit.getMonth(),
                limit.getCategory(),
                limit.getFamily(),
                limit.getLimit()
        );
    }

    @Override
    public LimitDTO setLimit(LimitDTO limit) {
        Limit limitModel = this.limitsDao.setLimit(fromDTO(limit));
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
}
