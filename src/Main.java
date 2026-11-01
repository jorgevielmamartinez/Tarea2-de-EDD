import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    long semilla = 12345L;
    int rangoMaximo = 100000;

    System.out.println("=== Serie 1: Variación de n (k = 10) ===");
    int[] valoresN = {100_000, 200_000, 500_000, 1_000_000};
    int kFijo = 10;
    for (int n : valoresN) {
      ejecutarConfiguracion(n, kFijo, semilla, rangoMaximo);
    }

    System.out.println("\n=== Serie 2: Variación de k (n = 5.000.000) ===");
    int nFijo = 5_000_000;
    int[] valoresK = {5, 10, 15, 20, 25, 30};
    for (int k : valoresK) {
      ejecutarConfiguracion(nFijo, k, semilla, rangoMaximo);
    }
  }

  private static void ejecutarConfiguracion(int n, int k, long semilla, int rango) {
    String nombreArchivo = "datos_n" + n + "_k" + k + ".txt";
    Archivos.generar(nombreArchivo, n, k, semilla, rango);
    int[][] arreglos = leerArchivo(nombreArchivo);

    System.out.printf("Configuración n = %d, k = %d%n", n, k);
    System.out.print("  k-MergeHeap -> ");
    medirAlgoritmo(arreglos, true);
    System.out.print("  k-MergeCB   -> ");
    medirAlgoritmo(arreglos, false);
  }

  private static void medirAlgoritmo(int[][] arreglos, boolean usarHeap) {
    for (int i = 0; i < 3; i++) {
      if (usarHeap) {
        kMergeHeap(arreglos);
      } else {
        kMergeCB(arreglos);
      }
    }

    final int REPETICIONES = 10;
    long tiempoTotal = 0;

    for (int i = 0; i < REPETICIONES; i++) {
      long inicio = System.nanoTime();
      int[] resultado;

      if (usarHeap) {
        resultado = kMergeHeap(arreglos);
      } else {
        resultado = kMergeCB(arreglos);
      }

      long fin = System.nanoTime();
      tiempoTotal += fin - inicio;

      // Validación fuera del cálculo de tiempo
      verificarResultado(arreglos, resultado);
    }

    double promedioMs = tiempoTotal / (double) REPETICIONES / 1_000_000.0;
    System.out.printf("Tiempo promedio: %.3f ms%n", promedioMs);
  }

  private static void verificarResultado(int[][] arreglos, int[] resultado) {
    int n = 0;
    for (int i = 0; i < arreglos.length; i++) {
      n += arreglos[i].length;
    }

    if (resultado.length != n) {
      throw new IllegalStateException("Error: El tamaño del arreglo resultante es incorrecto.");
    }

    for (int i = 1; i < resultado.length; i++) {
      if (resultado[i - 1] > resultado[i]) {
        throw new IllegalStateException("Error: El arreglo resultante no está ordenado.");
      }
    }
  }

  public static int[][] leerArchivo(String nombreArchivo) {
    int[][] arreglos = null;
    try {
      Scanner scanner = new Scanner(new File(nombreArchivo));
      int n = scanner.nextInt();
      int k = scanner.nextInt();
      arreglos = new int[k][];

      for (int i = 0; i < k; i++) {
        ArrayList<Integer> listaTemp = new ArrayList<>();
        int valor = scanner.nextInt();

        while (valor != -1) {
          listaTemp.add(valor);
          valor = scanner.nextInt();
        }

        arreglos[i] = new int[listaTemp.size()];
        for (int j = 0; j < listaTemp.size(); j++) {
          arreglos[i][j] = listaTemp.get(j);
        }
      }
      scanner.close();
    } catch (FileNotFoundException e) {
      System.err.println("Error: Archivo no encontrado - " + e.getMessage());
    }
    return arreglos;
  }

  private static int[] kMergeHeap(int[][] arreglo) {
    int k = arreglo.length;
    int n = 0;
    for (int i = 0; i < k; i++) {
      n += arreglo[i].length;
    }
    int[] B = new int[n];
    Heap heap = new Heap(k);
    Heap.Nodo[] iniciales = new Heap.Nodo[k];

    for (int i = 0; i < k; i++) {
      iniciales[i] = new Heap.Nodo(arreglo[i][0], i, 0);
    }
    heap.buildHeap(iniciales);

    for (int i = 0; i < n; i++) {
      Heap.Nodo min = heap.getMin();
      B[i] = min.valor;
      int arrId = min.idArreglo;
      int nextId = min.indiceElemento + 1;

      if (nextId < arreglo[arrId].length) {
        heap.reemplazarRaiz(new Heap.Nodo(arreglo[arrId][nextId], arrId, nextId));
      } else {
        heap.heapExtractMin();
      }
    }
    return B;
  }

  private static int[] kMergeCB(int[][] arreglos) {
    int k = arreglos.length;
    int n = 0;
    for (int i = 0; i < k; i++) {
      n += arreglos[i].length;
    }
    int[] B = new int[n];
    ColaBinomial temp = new ColaBinomial();

    for (int i = 0; i < k; i++) {
      temp.insertar(new ColaBinomial.NodoCB(arreglos[i][0], i, 0));
    }

    for (int i = 0; i < n; i++) {
      ColaBinomial.NodoCB minimo = temp.extraerMinimo();
      B[i] = minimo.valor;
      int arregloId = minimo.idArreglo;
      int nextIndice = minimo.indiceElemento + 1;

      if (nextIndice < arreglos[arregloId].length) {
        temp.insertar(new ColaBinomial.NodoCB(arreglos[arregloId][nextIndice], arregloId, nextIndice));
      }
    }
    return B;
  }
}