package agh.ics.oop.model;

import java.util.*;

public class Genome {
    private final int genomeLength;
    private List<Integer> genes = new ArrayList<>();

    public Genome(int genomeLength) {
        this.genes = generateGenome(genomeLength);
        this.genomeLength = genomeLength;
    }

    public Genome(List<Integer> genes) {
        this.genes = new ArrayList<>(genes);
        this.genomeLength = genes.size();
    }

    private List<Integer> generateGenome(int genomeLength) {
        for (int i = 0; i < genomeLength; i++) {
            Random number = new Random();
            genes.add(number.nextInt(8));
        }
        return genes;
    }

    public Genome crossover(int genomeRatio, List<Animal> parents) {
        int sideIndex = (int) Math.round(Math.random());
        int otherIndex = Math.abs(sideIndex - 1);
        List<Integer> parentGenes = parents.get(sideIndex).getGenome().getGenes();
        Genome childGenome = new Genome(parentGenes);
        List<Integer> childGenes = new ArrayList<>(childGenome.getGenes());
        for (int i = genomeRatio; i < childGenome.getGenes().size(); i++) {
            childGenes.set(i, parents.get(otherIndex).getGenome().getGenes().get(i));
        }
        childGenome.setGenes(childGenes);
        return childGenome;
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

    public void setGenes(List<Integer> genes) {
        this.genes = genes;
    }
}