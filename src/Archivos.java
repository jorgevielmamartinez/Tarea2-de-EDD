import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;
import java.util.Random;
import java.util.Arrays;
public class Archivos {
    public static void generar(String nombreArchivo, int n, int k, long semilla, int rangoMaximo) {
        if (n < k) {
            throw new IllegalArgumentException("n debe ser mayor o igual a k (n >= k)");
        }
        Random random = new Random(semilla);
        int[] tamanios = new int[k];
        Arrays.fill(tamanios, 1);
        int elementosRestantes = n - k;
        while (elementosRestantes > 0) {
            int indiceAleatorio = random.nextInt(k);
            tamanios[indiceAleatorio]++;
            elementosRestantes--;
        }
        try (PrintWriter writer = new PrintWriter(new FileWriter(nombreArchivo))) {
            writer.println(n + " " + k);
            for (int i = 0; i < k; i++) {
                int[] arregloActual = new int[tamanios[i]];
                for (int j = 0; j < tamanios[i]; j++) {
                    arregloActual[j] = random.nextInt(rangoMaximo) + 1;
                }
                Arrays.sort(arregloActual);
                for (int num : arregloActual) {
                    writer.print(num + " ");
                }
                writer.println("-1");
            }
        } catch (IOException e) {
            System.err.println("Error al generar el archivo: " + e.getMessage());
        }
    }
}

