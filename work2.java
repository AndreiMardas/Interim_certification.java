import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class Toy {
    private int id;
    private String name;
    private int quantity;
    private double frequency;

    public Toy(int id, String name, int quantity, double frequency) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.frequency = frequency;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getFrequency() {
        return frequency;
    }

    public void setFrequency(double frequency) {
        this.frequency = frequency;
    }
}

public class ToyStore {
    private List<Toy> toys;
    private List<Toy> prizeToys;

    public ToyStore() {
        toys = new ArrayList<>();
        prizeToys = new ArrayList<>();
    }

    public void addToy(Toy toy) {
        toys.add(toy);
    }

    public void updateToyFrequency(int toyId, double frequency) {
        for (Toy toy : toys) {
            if (toy.getId() == toyId) {
                toy.setFrequency(frequency);
                break;
            }
        }
    }

    public Toy selectPrizeToy() {
        double totalFrequency = 0;
        for (Toy toy : toys) {
            totalFrequency += toy.getFrequency();
        }

        Random random = new Random();
        double randomNumber = random.nextDouble() * totalFrequency;

        for (Toy toy : toys) {
            randomNumber -= toy.getFrequency();
            if (randomNumber <= 0) {
                return toy;
            }
        }

        return null; // Если игрушек нет
    }

    public void awardPrizeToy() {
        if (prizeToys.isEmpty()) {
            System.out.println("Нет призовых игрушек для выдачи.");
            return;
        }

        Toy prizeToy = prizeToys.remove(0);
        decreaseToyQuantity(prizeToy.getId());
        writePrizeToyToFile(prizeToy);
        System.out.println("Призовая игрушка \"" + prizeToy.getName() + "\" была выдана.");
    }

    private void decreaseToyQuantity(int toyId) {
        for (Toy toy : toys) {
            if (toy.getId() == toyId) {
                toy.quantity--;
                break;
            }
        }
    }

    private void writePrizeToyToFile(Toy toy) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("prize_toys.txt", true))) {
            writer.write(toy.getName() + System.lineSeparator());
        } catch (IOException e) {
            System.out.println("Ошибка при записи в файл.");
        }
    }

    public static void main(String[] args) {
        ToyStore toyStore = new ToyStore();

        // Добавление игрушек в магазин
        toyStore.addToy(new Toy(1, "Мяч", 10, 20));
        toyStore.addToy(new Toy(2, "Кукла", 5, 15));
        toyStore.addToy(new Toy(3, "Машинка", 8, 25));
        toyStore.addToy(new Toy(4, "Пазл", 15, 30));
        toyStore.addToy(new Toy(5, "Кубики", 12, 10));
        
        // Изменение частоты выпадения игрушек
        toyStore.updateToyFrequency(1, 15);
        toyStore.updateToyFrequency(4, 35);

        // Розыгрыш игрушек
        for (int i = 0; i < 5; i++) {
            Toy prizeToy = toyStore.selectPrizeToy();
            if (prizeToy != null) {
                toyStore.prizeToys.add(prizeToy);
            }
        }

        // Выдача призовой игрушки и запись в файл
        toyStore.awardPrizeToy();
    }
}