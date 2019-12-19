package ua.org.zagoruiko.expenses.goalsservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.org.zagoruiko.expenses.goalsservice.dao.ReportDao;
import ua.org.zagoruiko.expenses.goalsservice.dto.ReportItemDTO;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReportServiceImpl implements ReportService {
    private ReportDao reportDao;

    @Autowired
    public ReportServiceImpl(ReportDao reportDao) {
        this.reportDao = reportDao;
    }

    @Override
    public List<ReportItemDTO> currentMonthReport() {
        return this.reportDao.currentMonthReport().stream().map(r ->
                new ReportItemDTO(r.getYear(), r.getMonth(), r.getAmount(), r.getCategory()))
                .collect(Collectors.toList());
    }

    @Override
    public List<ReportItemDTO> currentYearByMonthReport() {
        return this.reportDao.currentYearByMonthReport().stream().map(r ->
                new ReportItemDTO(r.getYear(), r.getMonth(), r.getAmount(), r.getCategory()))
                .collect(Collectors.toList());
    }

    @Override
    public List<ReportItemDTO> currentYearTotalReport() {
        return null;
    }

    @Override
    public List<ReportItemDTO> currentMonthReport(List<String> categories) {
        return null;
    }

    @Override
    public List<ReportItemDTO> currentYearTotalReport(List<String> categories) {
        return null;
    }
}
