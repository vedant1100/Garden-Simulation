package garden.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class Garden {
    private final Map<String, Bed> beds = new LinkedHashMap<>();

    public Bed addBed(String id) {
        Bed bed = new Bed(id);
        if (beds.putIfAbsent(id, bed) != null) throw new IllegalArgumentException("Duplicate bed: " + id);
        return bed;
    }

    public Bed getBed(String id) {
        Bed bed = beds.get(id);
        if (bed == null) throw new IllegalArgumentException("Unknown bed: " + id);
        return bed;
    }

    public List<Bed> getBeds() { return List.copyOf(beds.values()); }

    public List<Plant> getLivingPlants() {
        return beds.values().stream().flatMap(bed -> bed.getPlants().stream())
                .filter(Plant::isAlive).toList();
    }

    /** The simulation engine calls this once per completed simulated day. */
    public void completeDay() {
        beds.values().forEach(Bed::completeDay);
    }
}
