package agh.ics.oop.model;

import java.util.*;

public class Genome {
    private int genomeLength; // Ustawia użytkownik
    private int min = 3;
    private int max = 5;

    private List<Integer> genes = new ArrayList<>();

    public Genome(int genomeLength) {
        this.genes = generateGenome(genomeLength);
        this.genomeLength = genomeLength;
    }

    public Genome(List<Integer> genes) {
        this.genes = new ArrayList<>(genes);
        this.genomeLength = genes.size();
    }

    public List<Integer> getGenes() {
        return genes;
    }

    private List<Integer> generateGenome(int genomeLength) {
        for (int i = 0; i < genomeLength; i++) {
            Random number = new Random();
            genes.add(number.nextInt(8));
        }
        return genes;
    }

    public Genome crossover(int genomRatio, List<Animal> parents) {
        int sideIndex = (int) Math.round(Math.random());
        int otherIndex = Math.abs(sideIndex - 1);
        List<Integer> parentGenes = parents.get(sideIndex).getGenome().getGenes();
        Genome childGenome = new Genome(parentGenes);
        List<Integer> childGenes = new ArrayList<>(childGenome.getGenes());

        for (int i = genomRatio; i < childGenome.getGenes().size(); i++) {
            childGenes.set(i, parents.get(otherIndex).getGenome().getGenes().get(i));
        }

        childGenome.setGenes(childGenes);
        return childGenome;
    }

    // Mutacje genomu
    public void mutate1() {
        RandomMutationPointsGenerator randomMutationPointsGenerator = new RandomMutationPointsGenerator(min, max, genomeLength);
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

    public void mutate2() {
        RandomMutationPointsGenerator randomMutationPointsGenerator = new RandomMutationPointsGenerator(min, max, genomeLength);
        for (int point : randomMutationPointsGenerator) {
            if (Math.random() < 0.5) {
                genes.set(point, (genes.get(point) + 1) % 8);
            } else {
                genes.set(point, (genes.get(point) - 1 + 8) % 8);
            }
        }
    }

    public void setGenes(List<Integer> genes) {
        this.genes = genes;
    }
    /* wybieranie między wariantem mutacji
    logika: getText z pola i in jeśli 1 to wykonujemy mutate1
     */
}