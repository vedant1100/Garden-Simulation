package garden.model;

import java.util.Set;

/** Per-variety simulation settings. Stage thresholds count healthy simulated days since sowing. */
public record PlantProfile(String name, int waterRequirement, int minimumF, int maximumF,
                           int germinationDay, int growingDay, int maturityDay, Set<String> parasites) {
    public static final PlantProfile TOMATO = new PlantProfile("Tomato", 15, 60, 85, 5, 15, 60, Set.of("aphid", "whitefly"));
    public static final PlantProfile ROSE = new PlantProfile("Rose", 10, 55, 85, 12, 30, 90, Set.of("aphid"));
    public static final PlantProfile BASIL = new PlantProfile("Basil", 8, 65, 85, 4, 12, 40, Set.of("whitefly"));
    public static final PlantProfile LETTUCE = new PlantProfile("Lettuce", 12, 45, 75, 3, 10, 35, Set.of("aphid", "slug"));

    public PlantProfile {
        if (name == null || name.isBlank() || waterRequirement <= 0 || minimumF > maximumF
                || germinationDay < 1 || growingDay <= germinationDay || maturityDay <= growingDay) {
            throw new IllegalArgumentException("Invalid plant profile");
        }
        parasites = Set.copyOf(parasites);
        if (parasites.stream().anyMatch(String::isBlank)) {
            throw new IllegalArgumentException("Empty parasite name");
        }
    }

    GrowthStage stageAt(int healthyDays) {
        if (healthyDays >= maturityDay) return GrowthStage.MATURE;
        if (healthyDays >= growingDay) return GrowthStage.GROWING;
        if (healthyDays >= germinationDay) return GrowthStage.SEEDLING;
        return GrowthStage.SEED;
    }
}
