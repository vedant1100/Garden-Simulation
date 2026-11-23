package garden.model;

public class Plant {
    private final String id;
    private GrowthStage growthStage;

    public Plant(String id) {
        this.id = id;
        this.growthStage = GrowthStage.SEED;
    }

    public String getId() {
        return id;
    }

    public GrowthStage getGrowthStage() {
        return growthStage;
    }
}