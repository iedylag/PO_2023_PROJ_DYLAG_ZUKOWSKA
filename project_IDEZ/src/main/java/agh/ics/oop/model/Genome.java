package agh.ics.oop.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Genome {
    static final int GENOME_LENGTH = 10; // Ustawia użytkownik
    static final int MIN_MUTATIONS = 2;
    static final int MAX_MUTATIONS = 6;

    List<Integer> genes = new ArrayList<>();

    public Genome() {
        this.genes = generateGenome();
    }

    public List<Integer> getGenes() {
        return genes;
    }

    private List<Integer> generateGenome() {
        for (int i = 0; i < GENOME_LENGTH; i++) {
            Random number = new Random();
            genes.add(number.nextInt(8));
        }
        return genes;
    }

    Genome crossover(int genomRatio, List<Animal> parents) {
        int sideIndex = (int) Math.round(Math.random());
        int otherIndex = Math.abs(sideIndex - 1);
        Genome childGenome = parents.get(sideIndex).getGenome();
        for (int i = genomRatio; i < GENOME_LENGTH; i++) {
            childGenome.getGenes().remove(i);
            childGenome.getGenes().add(parents.get(otherIndex).getGenome().getGenes().get(i));
        }
        return childGenome;
    }

    // Mutacje genomu
    void mutate1() {
        RandomMutationPointsGenerator randomMutationPointsGenerator = new RandomMutationPointsGenerator(MIN_MUTATIONS, MAX_MUTATIONS, GENOME_LENGTH);
        for (int point : randomMutationPointsGenerator) {
            genes.remove(point);
            genes.add(point, new Random().nextInt(8));
        }
    }

    /*
     * MUTACJE
     * [obowiązkowo dla wszystkich] pełna losowość - mutacja zmienia gen na dowolny
     * inny gen; [1] lekka korekta - mutacja zmienia gen o 1 w górę lub w dół (np.
     * gen 3 może zostać zamieniony na 2 lub 4, a gen 0 na 1 lub 7);
     */

    void mutate2() {
        RandomMutationPointsGenerator randomMutationPointsGenerator = new RandomMutationPointsGenerator(MIN_MUTATIONS, MAX_MUTATIONS, GENOME_LENGTH);
        for (int point : randomMutationPointsGenerator) {
            if (Math.random() < 0.5) {
                genes.set(point, (genes.get(point) + 1) % 8);
            } else {
                genes.set(point, (genes.get(point) - 1 + 8) % 8);
            }
        }
    }
    /* wybieranie między wariantem mutacji
    logika: getText z pola i in jeśli 1 to wykonujemy mutate1
     */
}