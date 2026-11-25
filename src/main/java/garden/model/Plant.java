package garden.model;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public final class Plant {
    private final String id;
    private final PlantProfile profile;
    private final Set<String> infestations = new HashSet<>();
    private int health = 100;
    private int healthyDays;

    Plant(String id, PlantProfile profile, boolean seedling) {
        if (id == null || id.isBlank()) throw new IllegalArgumentException("Missing plant ID");
        this.id = id;
        this.profile = Objects.requireNonNull(profile);
        healthyDays = seedling ? profile.germinationDay() : 0;
    }

    public String getId() { return id; }
    public PlantProfile getProfile() { return profile; }
    public GrowthStage getGrowthStage() { return profile.stageAt(healthyDays); }
    public int getHealth() { return health; }
    public boolean isAlive() { return health > 0; }
    public Set<String> getInfestations() { return Set.copyOf(infestations); }

    public void exposeTo(String parasite) {
        if (isAlive() && profile.parasites().contains(parasite)) infestations.add(parasite);
    }

    public void treat(String parasite) {
        infestations.remove(parasite);
    }

    void completeDay(double waterFraction, int temperatureF) {
        if (!isAlive()) return;
        int damage = (int) Math.ceil(12 * (1 - waterFraction));
        if (temperatureF < profile.minimumF() || temperatureF > profile.maximumF()) damage += 8;
        damage += 4 * infestations.size();
        if (damage > 0) {
            health = Math.max(0, health - damage);
        } else {
            health = Math.min(100, health + 2);
            if (healthyDays < profile.maturityDay()) healthyDays++;
        }
    }
}