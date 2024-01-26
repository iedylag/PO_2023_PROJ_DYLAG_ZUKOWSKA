package agh.ics.oop.model;

import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

class GenomeTest {

    @Test
    void genomeHasCorrectLength() {
        // given
        int genomeLength = 10; // Example length
        Genome genome = new Genome(genomeLength);

        // when-then
        assertEquals(genomeLength, genome.getGenes().size());
    }
}
