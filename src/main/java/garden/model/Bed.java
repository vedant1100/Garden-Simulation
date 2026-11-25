package garden.model;

import java.util.ArrayList;
import java.util.List;

public final class Bed {
    private final String id;
    private final List<Plant> plants = new ArrayList<>();
    private long waterUnits;
    private int temperatureF = 70;

    public Bed(String id) {
        if (id == null || id.isBlank()) throw new IllegalArgumentException("Missing bed ID");
        this.id = id;
    }

    public String getId() { return id; }
    public List<Plant> getPlants() { return List.copyOf(plants); }
    public long getWaterUnits() { return waterUnits; }
    public int getTemperatureF() { return temperatureF; }
    public void setTemperatureF(int value) { temperatureF = value; }

    public Plant sow(PlantProfile profile) { return addPlant(profile, false); }
    public Plant addSeedling(PlantProfile profile) { return addPlant(profile, true); }

    private Plant addPlant(PlantProfile profile, boolean seedling) {
        Plant plant = new Plant(id + "-" + (plants.size() + 1), profile, seedling);
        plants.add(plant);
        return plant;
    }

    public void water(int units) {
        if (units < 0) throw new IllegalArgumentException("Negative water dose");
        waterUnits = Math.addExact(waterUnits, units);
    }

    /** Plants share available water in proportion to their individual demand. */
    public void completeDay() {
        long demand = plants.stream().filter(Plant::isAlive)
                .mapToLong(p -> p.getProfile().waterRequirement()).sum();
        long used = Math.min(demand, waterUnits);
        double fraction = demand == 0 ? 1 : (double) used / demand;
        for (Plant plant : plants) plant.completeDay(fraction, temperatureF);
        waterUnits -= used;
    }
}
