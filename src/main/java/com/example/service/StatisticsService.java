package com.example.service;

import org.springframework.stereotype.Service;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class StatisticsService {
    private final CompetitionService competitionService;
    private final InnovationProjectService innovationProjectService;
    private final SoftwareCopyrightService softwareCopyrightService;
    private final AcademicPaperService academicPaperService;

    public StatisticsService(CompetitionService competitionService, InnovationProjectService innovationProjectService,
                             SoftwareCopyrightService softwareCopyrightService, AcademicPaperService academicPaperService) {
        this.competitionService = competitionService;
        this.innovationProjectService = innovationProjectService;
        this.softwareCopyrightService = softwareCopyrightService;
        this.academicPaperService = academicPaperService;
    }

    public Map<String, Object> getOverview() {
        Map<String, Object> overview = new LinkedHashMap<>();
        overview.put("competitionCount", competitionService.countAll());
        overview.put("innovationCount", innovationProjectService.countAll());
        overview.put("softwareCount", softwareCopyrightService.countAll());
        overview.put("paperCount", academicPaperService.countAll());
        return overview;
    }

    public Map<String, Object> getCompetitionStats() {
        Map<String, Object> stats = new LinkedHashMap<>();

        Map<String, Integer> categoryStats = new LinkedHashMap<>();
        categoryStats.put("A类", competitionService.countByCategory("A"));
        categoryStats.put("B类", competitionService.countByCategory("B"));
        categoryStats.put("C类", competitionService.countByCategory("C"));
        stats.put("byCategory", categoryStats);

        Map<String, Integer> levelStats = new LinkedHashMap<>();
        levelStats.put("国家级", competitionService.countByLevel("国家级"));
        levelStats.put("省级", competitionService.countByLevel("省级"));
        levelStats.put("市级", competitionService.countByLevel("市级"));
        levelStats.put("校级", competitionService.countByLevel("校级"));
        levelStats.put("院级", competitionService.countByLevel("院级"));
        stats.put("byLevel", levelStats);

        return stats;
    }

    public Map<String, Object> getInnovationStats() {
        Map<String, Object> stats = new LinkedHashMap<>();

        Map<String, Integer> levelStats = new LinkedHashMap<>();
        levelStats.put("国家级", innovationProjectService.countByLevel("国家级"));
        levelStats.put("省级", innovationProjectService.countByLevel("省级"));
        levelStats.put("校级", innovationProjectService.countByLevel("校级"));
        levelStats.put("院级", innovationProjectService.countByLevel("院级"));
        stats.put("byLevel", levelStats);

        Map<String, Integer> typeStats = new LinkedHashMap<>();
        typeStats.put("创新训练项目", innovationProjectService.countByType("创新训练项目"));
        typeStats.put("创业训练项目", innovationProjectService.countByType("创业训练项目"));
        typeStats.put("创业实践项目", innovationProjectService.countByType("创业实践项目"));
        stats.put("byType", typeStats);

        return stats;
    }

    public Map<String, Object> getPaperStats() {
        Map<String, Object> stats = new LinkedHashMap<>();

        String[] levels = {"CCF A类会议", "CCF B类会议", "CCF C类会议", "EI会议",
                "SCI一区", "SCI二区", "SCI三区", "SCI四区", "EI期刊", "北大核心期刊", "省级期刊"};
        Map<String, Integer> levelStats = new LinkedHashMap<>();
        for (String level : levels) {
            levelStats.put(level, academicPaperService.countByLevel(level));
        }
        stats.put("byLevel", levelStats);

        return stats;
    }
}
