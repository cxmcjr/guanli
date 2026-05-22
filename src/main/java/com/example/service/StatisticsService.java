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
        return getOverview(null);
    }

    public Map<String, Object> getOverview(String realName) {
        Map<String, Object> overview = new LinkedHashMap<>();
        if (realName == null) {
            overview.put("competitionCount", competitionService.countAll());
            overview.put("innovationCount", innovationProjectService.countAll());
            overview.put("softwareCount", softwareCopyrightService.countAll());
            overview.put("paperCount", academicPaperService.countAll());
        } else {
            overview.put("competitionCount", competitionService.findAll().stream()
                    .filter(c -> realName.equals(c.getSubmitter()) || isInList(realName, c.getParticipants())).count());
            overview.put("innovationCount", innovationProjectService.findAll().stream()
                    .filter(p -> realName.equals(p.getSubmitter()) || isInList(realName, p.getMembers())).count());
            overview.put("softwareCount", softwareCopyrightService.findAll().stream()
                    .filter(s -> realName.equals(s.getSubmitter()) || realName.equals(s.getAuthor())).count());
            overview.put("paperCount", academicPaperService.findAll().stream()
                    .filter(p -> realName.equals(p.getSubmitter()) || isInList(realName, p.getAuthors())).count());
        }
        return overview;
    }

    public Map<String, Object> getCompetitionStats() {
        return getCompetitionStats(null);
    }

    public Map<String, Object> getCompetitionStats(String realName) {
        Map<String, Object> stats = new LinkedHashMap<>();

        if (realName == null) {
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
        } else {
            var all = competitionService.findAll();
            Map<String, Integer> categoryStats = new LinkedHashMap<>();
            categoryStats.put("A类", (int) all.stream().filter(c -> "A".equals(c.getCategory())
                    && (realName.equals(c.getSubmitter()) || isInList(realName, c.getParticipants()))).count());
            categoryStats.put("B类", (int) all.stream().filter(c -> "B".equals(c.getCategory())
                    && (realName.equals(c.getSubmitter()) || isInList(realName, c.getParticipants()))).count());
            categoryStats.put("C类", (int) all.stream().filter(c -> "C".equals(c.getCategory())
                    && (realName.equals(c.getSubmitter()) || isInList(realName, c.getParticipants()))).count());
            stats.put("byCategory", categoryStats);

            Map<String, Integer> levelStats = new LinkedHashMap<>();
            levelStats.put("国家级", (int) all.stream().filter(c -> "国家级".equals(c.getAwardLevel())
                    && (realName.equals(c.getSubmitter()) || isInList(realName, c.getParticipants()))).count());
            levelStats.put("省级", (int) all.stream().filter(c -> "省级".equals(c.getAwardLevel())
                    && (realName.equals(c.getSubmitter()) || isInList(realName, c.getParticipants()))).count());
            levelStats.put("市级", (int) all.stream().filter(c -> "市级".equals(c.getAwardLevel())
                    && (realName.equals(c.getSubmitter()) || isInList(realName, c.getParticipants()))).count());
            levelStats.put("校级", (int) all.stream().filter(c -> "校级".equals(c.getAwardLevel())
                    && (realName.equals(c.getSubmitter()) || isInList(realName, c.getParticipants()))).count());
            levelStats.put("院级", (int) all.stream().filter(c -> "院级".equals(c.getAwardLevel())
                    && (realName.equals(c.getSubmitter()) || isInList(realName, c.getParticipants()))).count());
            stats.put("byLevel", levelStats);
        }

        return stats;
    }

    public Map<String, Object> getInnovationStats() {
        return getInnovationStats(null);
    }

    public Map<String, Object> getInnovationStats(String realName) {
        Map<String, Object> stats = new LinkedHashMap<>();

        if (realName == null) {
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
        } else {
            var all = innovationProjectService.findAll();
            Map<String, Integer> levelStats = new LinkedHashMap<>();
            levelStats.put("国家级", (int) all.stream().filter(p -> "国家级".equals(p.getLevel())
                    && (realName.equals(p.getSubmitter()) || isInList(realName, p.getMembers()))).count());
            levelStats.put("省级", (int) all.stream().filter(p -> "省级".equals(p.getLevel())
                    && (realName.equals(p.getSubmitter()) || isInList(realName, p.getMembers()))).count());
            levelStats.put("校级", (int) all.stream().filter(p -> "校级".equals(p.getLevel())
                    && (realName.equals(p.getSubmitter()) || isInList(realName, p.getMembers()))).count());
            levelStats.put("院级", (int) all.stream().filter(p -> "院级".equals(p.getLevel())
                    && (realName.equals(p.getSubmitter()) || isInList(realName, p.getMembers()))).count());
            stats.put("byLevel", levelStats);

            Map<String, Integer> typeStats = new LinkedHashMap<>();
            typeStats.put("创新训练项目", (int) all.stream().filter(p -> "创新训练项目".equals(p.getType())
                    && (realName.equals(p.getSubmitter()) || isInList(realName, p.getMembers()))).count());
            typeStats.put("创业训练项目", (int) all.stream().filter(p -> "创业训练项目".equals(p.getType())
                    && (realName.equals(p.getSubmitter()) || isInList(realName, p.getMembers()))).count());
            typeStats.put("创业实践项目", (int) all.stream().filter(p -> "创业实践项目".equals(p.getType())
                    && (realName.equals(p.getSubmitter()) || isInList(realName, p.getMembers()))).count());
            stats.put("byType", typeStats);
        }

        return stats;
    }

    public Map<String, Object> getPaperStats() {
        return getPaperStats(null);
    }

    public Map<String, Object> getPaperStats(String realName) {
        Map<String, Object> stats = new LinkedHashMap<>();

        String[] levels = {"CCF A类会议", "CCF B类会议", "CCF C类会议", "EI会议",
                "SCI一区", "SCI二区", "SCI三区", "SCI四区", "EI期刊", "北大核心期刊", "省级期刊"};
        Map<String, Integer> levelStats = new LinkedHashMap<>();

        if (realName == null) {
            for (String level : levels) {
                levelStats.put(level, academicPaperService.countByLevel(level));
            }
        } else {
            var all = academicPaperService.findAll();
            for (String level : levels) {
                String l = level;
                levelStats.put(level, (int) all.stream().filter(p -> l.equals(p.getJournalLevel())
                        && (realName.equals(p.getSubmitter()) || isInList(realName, p.getAuthors()))).count());
            }
        }
        stats.put("byLevel", levelStats);

        return stats;
    }

    private boolean isInList(String name, String list) {
        if (list == null || list.isEmpty()) return false;
        for (String s : list.split("[,，]")) {
            if (s.trim().equals(name)) return true;
        }
        return false;
    }
}
