package agh.ics.oop.model;

import java.util.*;

public class Genome {
    private final int genomeLength;
    private List<Integer> genes;

    public Genome(int genomeLength) {
        this.genomeLength = genomeLength;
        generateGenome();
    }

    public Genome(List<Integer> genes) {
        this.genes = new ArrayList<>(genes);
        this.genomeLength = genes.size();
    }

    private void generateGenome() {
        genes = new ArrayList<>(genomeLength);
        Random number = new Random();
        for (int i = 0; i < genomeLength; i++) {
            genes.add(number.nextInt(8));
        }
    }

    public Genome crossover(int genomeRatio, List<Animal> parents) {
        int sideIndex = (int) Math.round(Math.random());
        int otherIndex = 1 - sideIndex;
        List<Integer> parentGenes = parents.get(sideIndex).getGenome().getGenes();

        List<Integer> childGenes = new ArrayList<>(parentGenes);

        for (int i = genomeRatio; i < childGenes.size(); i++) {
            childGenes.set(i, parents.get(otherIndex).getGenome().getGenes().get(i));
        }

        return new Genome(childGenes);
    }

    public void mutate1(int minMutation, int maxMutation) {
        RandomMutationPointsGenerator randomMutationPointsGenerator = new RandomMutationPointsGenerator(minMutation, maxMutation, genomeLength);
        for (int point : randomMutationPointsGenerator) {
            genes.remove(point);
            genes.add(point, new Random().nextInt(8));
        }
    }

    public void mutate2(int minMutation, int maxMutation) {
        RandomMutationPointsGenerator randomMutationPointsGenerator = new RandomMutationPointsGenerator(minMutation, maxMutation, genomeLength);
        for (int point : randomMutationPointsGenerator) {
            if (Math.random() < 0.5) {
                genes.set(point, (genes.get(point) + 1) % 8);
            } else {
                genes.set(point, (genes.get(point) - 1 + 8) % 8);
            }
        }
    }

    public List<Integer> getGenes() {
        return genes;
    }
}