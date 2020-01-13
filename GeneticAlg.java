package com.company;

import java.util.ArrayList;
import java.util.List;

class Artefact {
  private String name;
  private double price;
  private double value;

  public Artefact(String name, double price, double value) {
    this.name = name;
    this.price = price;
    this.value = value;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public double getPrice() {
    return price;
  }

  public void setPrice(double price) {
    this.price = price;
  }

  public double getValue() {
    return value;
  }

  public void setValue(double value) {
    this.value = value;
  }
}

class GameGA {
  private List<Artefact> artefacts;
  private int artefactsLength;
  private double maxGold;
  private int populationSize;
  private List<List<Integer>> population;
  private List<List<Integer>> newPopulation;
  private List<Double> populationFitness;
  private List<Double> bestFitessForGeneration;
  private List<List<Integer>> bestSolutionForGeneration;
  private double mutationProbability;
  private double crossoverProbability;
  private int maxGenerations;
  private double totalFitnessOfGeneration;
  private int currentGenerations;


  public GameGA() {
    artefacts = new ArrayList<>();
    artefacts.add(new Artefact("Map", 100, 150));
    artefacts.add(new Artefact("Compass", 120, 40));
    artefacts.add(new Artefact("Magic water", 1600, 200));
    artefacts.add(new Artefact("Magic boots", 700, 160));
    artefacts.add(new Artefact("Magic Bow", 150, 60));
    artefacts.add(new Artefact("Flying carpet", 680, 45));
    artefacts.add(new Artefact("Magic Ring", 270, 60));
    artefacts.add(new Artefact("Magic Glasses", 385, 48));
    artefacts.add(new Artefact("Life elixir", 230, 30));
    artefacts.add(new Artefact("Magic animal", 520, 10));
    artefacts.add(new Artefact("Sword", 1700, 400));
    artefacts.add(new Artefact("Shield", 500, 300));
    artefacts.add(new Artefact("Teleport", 240, 15));
    artefacts.add(new Artefact("Magic berries", 480, 10));
    artefacts.add(new Artefact("Magic umbrella", 730, 40));
    artefacts.add(new Artefact("Flower", 420, 70));
    artefacts.add(new Artefact("Knife", 430, 75));
    artefacts.add(new Artefact("Tent", 220, 80));
    artefacts.add(new Artefact("Garlic", 70, 20));
    artefacts.add(new Artefact("Silver elixir", 180, 12));
    artefacts.add(new Artefact("Magic hat", 40, 50));
    artefacts.add(new Artefact("Magic beer", 300, 10));
    artefacts.add(new Artefact("Silver arrows", 900, 20));
    artefacts.add(new Artefact("Mystic orb", 2000, 150));
    artefactsLength = artefacts.size();
    maxGold = 5000;
    populationSize = 100;
    population = new ArrayList<>();
    newPopulation = new ArrayList<>();
    populationFitness = new ArrayList<>();
    bestFitessForGeneration = new ArrayList<>();
    bestSolutionForGeneration = new ArrayList<>();
    mutationProbability = 0.02;
    crossoverProbability = 0.08;
    maxGenerations = 200;
    currentGenerations = 0;
  }

  public void solveProblem() {
    // Population with random 0s and 1s - 1st generation
    makeInitialPopulation();

    System.out.println("First generation: ");
    for (int i = 0; i < populationSize; i++) {
      System.out.println((i + 1) + " - " + population.get(i));
    }

    evolveInitialPopulation();

    System.out.println("First generation Fitness: ");
    for (int i = 0; i < populationSize; i++) {
      System.out.println((i + 1) + " - " + populationFitness.get(i));
    }

    bestSolutionForGeneration.add(population.get(getIndexOfSolution()));
    bestFitessForGeneration.add(fitness(population.get(getIndexOfSolution())));
    System.out.println("Fitness score of best solution of initial generation: " + bestFitessForGeneration.get(0));


    if (maxGenerations > 1) {
      makeNextGenerations();
    }

  }

  public void makeInitialPopulation() {
    for (int i = 0; i < populationSize; i++) {
      // Calls makeGene() once for each element position
      population.add(makeGene());
    }
  }

  //
  public List<Integer> makeGene() {
    List<Integer> gene = new ArrayList<>();
    for (int i = 0; i < artefactsLength; i++) {
      gene.add((int) (Math.random() * 2));
    }
    return gene;
  }

  public void evolveInitialPopulation() {
    totalFitnessOfGeneration = 0;
    for (int i = 0; i < populationSize; i++) {
      double tempFitnessGene = fitness(population.get(i));
      populationFitness.add(tempFitnessGene);
      totalFitnessOfGeneration += tempFitnessGene;
    }
  }

  public void evolveNewPopulation() {
    totalFitnessOfGeneration = 0;
    for (int i = 0; i < populationSize; i++) {
      double tempFitnessGene = fitness(population.get(i));
      populationFitness.add(tempFitnessGene);
      totalFitnessOfGeneration += tempFitnessGene;
    }
  }

  public double fitness(List<Integer> gene) {
    double totalValue = 0;
    double totalPrice = 0;

    for (int i = 0; i < artefactsLength; i++) {
      if (gene.get(i) == 1) {
        totalPrice += artefacts.get(i).getPrice();
        totalValue += artefacts.get(i).getValue();
      }
    }
    double fitnessValue = 0;
    if (maxGold - totalPrice >= 0) {
      fitnessValue = totalValue;
    }

    return fitnessValue;
  }

  private void mutate() {
    double randomProbability = Math.random();
    if (randomProbability <= mutationProbability) {
      System.out.println("Mutation occured");
      int randomGene = (int) (Math.random() * newPopulation.size());
      int randomBit = (int) (Math.random() * artefactsLength);
      if (newPopulation.get(randomGene).get(randomBit) == 1) {
        newPopulation.get(randomGene).set(randomBit, 0);
      } else {
        newPopulation.get(randomGene).set(randomBit, 1);
      }
    }
  }

  private void makeNextGenerations() {
    for (int i = 1; i < maxGenerations; i++) {
//      if () previous 5 generations have the same fitness stop

      for (int j = 0; j < populationSize / 2; j++) {
        makeNextPopulation();
      }

      populationFitness.clear();

      evolveNewPopulation();

      for (int j = 0; j < populationSize; j++) {
        population.set(j, newPopulation.get(j));
      }

      System.out.println("Generation " + (i + 1) + " : ");
      for (int j = 0; j < populationSize; j++) {
        System.out.println((j + 1) + " - " + population.get(j));
      }

      System.out.println("Fitness: ");
      for (int j = 0; j < populationSize; j++) {
        System.out.println((j + 1) + " - " + populationFitness.get(j));
      }

      newPopulation.clear();

      bestSolutionForGeneration.add(population.get(getIndexOfSolution()));
      System.out.println("Best solution of generation " + (i + 1) + ": " + bestSolutionForGeneration.get(i));

      bestFitessForGeneration.add(fitness(population.get(getIndexOfSolution())));
      System.out.println("Fitness score of best solution of generation " + i + 1 + " : " + bestFitessForGeneration.get(i));


    }
  }

  private void makeNextPopulation() {
    // 2 genes for crossover
    int gene1 = selectGene();
    int gene2 = selectGene();

    currentGenerations++;

    crossover(gene1, gene2);


  }

  private void crossover(int geneIndex1, int geneIndex2) {
    double randomProbability = Math.random();
    List<Integer> gene1 = population.get(geneIndex1);
    List<Integer> gene2 = population.get(geneIndex2);
    if (randomProbability <= crossoverProbability) {
      int crossoverIndex = (int) (Math.random() * artefactsLength);
      List<Integer> childGene1 = new ArrayList<>(gene1.subList(0, crossoverIndex));
      childGene1.addAll(gene2.subList(crossoverIndex, artefactsLength));
      newPopulation.add(childGene1);
      List<Integer> childGene2 = new ArrayList<>(gene2.subList(0, crossoverIndex));
      childGene2.addAll(gene1.subList(crossoverIndex, artefactsLength));
      newPopulation.add(childGene2);
    } else {
      // clone one of the genes
      newPopulation.add(gene1);
      newPopulation.add(gene2);
    }

    mutate();
  }

  private int selectGene() {
    double probability = Math.random() * totalFitnessOfGeneration;
    for (int i = 0; i < populationSize; i++) {
      if (probability <= fitness(population.get(i))) {
        return i;
      }
      probability -= fitness(population.get(i));
    }
    return 0;
  }

  private int getIndexOfSolution() {
    int index = 0;
    double solution = 0.0;
    for (int i = 0; i < populationSize; i++) {
      double current = fitness(population.get(i));
      if (current > solution) {
        solution = current;
        index = i;
      }
    }
    return index;
  }
}

public class Main {

  public static void main(String[] args) {

    GameGA ga = new GameGA();
    ga.solveProblem();
  }
}
