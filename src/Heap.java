public class Heap {
    public static class Nodo {
        int valor;
        int idArreglo;
        int indiceElemento;
        public Nodo(int valor, int idArreglo, int indiceElemento) {
            this.valor = valor;
            this.idArreglo = idArreglo;
            this.indiceElemento = indiceElemento;
        }
    }
    private Nodo[] A;
    private int size;
    public Heap(int k) {
        this.A = new Nodo[k];
        this.size = 0;
    }
    private int left(int i) {
        return 2 * i + 1;
    }
    private int right(int i) {
        return 2 * i + 2;
    }
    private void heapify(int i) {
        int l = left(i);
        int r = right(i);
        int smallest = i;
        if (l < size && A[l].valor < A[smallest].valor) {
            smallest = l;
        }
        if (r < size && A[r].valor < A[smallest].valor) {
            smallest = r;
        }
        if (smallest != i) {
            Nodo temp = A[i];
            A[i] = A[smallest];
            A[smallest] = temp;
            heapify(smallest);
        }
    }
    public void buildHeap(Nodo[] datos) {
        this.A = datos;
        this.size = datos.length;
        for (int i = (size / 2) - 1; i >= 0; i--) {
            heapify(i);
        }
    }
    public Nodo heapExtractMin() {
        if (size < 1) {
            throw new IllegalStateException("Heap vacio");
        }
        Nodo min = A[0];
        A[0] = A[size - 1];
        size--;
        heapify(0);
        return min;
    }
    public void reemplazarRaiz(Nodo nuevoNodo) {
        A[0] = nuevoNodo;
        heapify(0);
    }
    public Nodo getMin() {
        if (size < 1) {
            throw new IllegalStateException("Heap vacio");
        }
        return A[0];
    }

    public static int[] K_MergeHeap(int [][]arreglo){
        int k=arreglo.length;
        int n=0;
        for(int i=0;i<k;i++){
            n+=arreglo[i].length;
        }
        int []B=new int[n];
        Heap heap=new Heap(k);
        Heap.Nodo[] iniciales=new Heap.Nodo[k];
        for(int i=0;i<k;i++){
            iniciales[i]=new Heap.Nodo(arreglo[i][0],i,0);
        }
        heap.buildHeap(iniciales);
        for(int i=0;i<n;i++){
            Heap.Nodo min=heap.getMin();
            B[i]=min.valor;
            int arrId=min.idArreglo;
            int nextId=min.indiceElemento+1;
            if (nextId<arreglo[arrId].length) {
                heap.reemplazarRaiz(new Heap.Nodo(arreglo[arrId][nextId],arrId,nextId));
            }else {
                heap.heapExtractMin();
            }
        }
        return B;
    }
}

