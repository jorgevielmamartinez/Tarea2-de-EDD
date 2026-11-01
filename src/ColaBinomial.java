public class ColaBinomial {
    public static class NodoCB {
        int valor;
        int idArreglo;
        int indiceElemento;
        int grado;
        NodoCB padre;
        NodoCB hijo;
        NodoCB hermano;
        public NodoCB(int valor, int idArreglo, int indiceElemento) {
            this.valor = valor;
            this.idArreglo = idArreglo;
            this.indiceElemento = indiceElemento;
            this.grado = 0;
            this.padre = null;
            this.hijo = null;
            this.hermano = null;
        }
    }

    private NodoCB head;
    public ColaBinomial() {
        this.head = null;
    }
    private void enlazar(NodoCB y, NodoCB z) {
        y.padre = z;
        y.hermano = z.hijo;
        z.hijo = y;
        z.grado++;
    }
    private NodoCB mezclarRaices(ColaBinomial otra) {
        if (this.head == null) {
            return otra.head;
        }
        if (otra.head == null) {
            return this.head;
        }
        NodoCB headMezclado;
        NodoCB actualThis = this.head;
        NodoCB actualOtra = otra.head;
        if (actualThis.grado <= actualOtra.grado) {
            headMezclado = actualThis;
            actualThis = actualThis.hermano;
        } else {
            headMezclado = actualOtra;
            actualOtra = actualOtra.hermano;
        }
        NodoCB actualMezclado = headMezclado;
        while (actualThis != null && actualOtra != null) {
            if (actualThis.grado <= actualOtra.grado) {
                actualMezclado.hermano = actualThis;
                actualThis = actualThis.hermano;
            } else {
                actualMezclado.hermano = actualOtra;
                actualOtra = actualOtra.hermano;
            }
            actualMezclado = actualMezclado.hermano;
        }
        actualMezclado.hermano = (actualThis != null) ? actualThis : actualOtra;
        return headMezclado;
    }
    public void unir(ColaBinomial otra) {
        if (otra == null || otra.head == null) return;
        this.head = mezclarRaices(otra);
        if (this.head == null) return;
        NodoCB prev = null;
        NodoCB x = this.head;
        NodoCB next = x.hermano;
        while (next != null) {
            if (x.grado != next.grado || (next.hermano != null && next.hermano.grado == x.grado)) {
                prev = x;
                x = next;
            }
            else if (x.valor <= next.valor) {
                x.hermano = next.hermano;
                enlazar(next, x);
            }
            else {
                if (prev == null) {
                    this.head = next;
                } else {
                    prev.hermano = next;
                }
                enlazar(x, next);
                x = next;
            }
            next = x.hermano;
        }
    }
    public void insertar(NodoCB nodo) {
        ColaBinomial temporal = new ColaBinomial();
        temporal.head = nodo;
        this.unir(temporal);
    }
    public NodoCB extraerMinimo() {
        if (head == null) return null;
        NodoCB min = head;
        NodoCB minPrev = null;
        NodoCB prev = head;
        NodoCB curr = head.hermano;
        while (curr != null) {
            if (curr.valor < min.valor) {
                min = curr;
                minPrev = prev;
            }
            prev = curr;
            curr = curr.hermano;
        }
        if (minPrev == null) {
            head = min.hermano;
        } else {
            minPrev.hermano = min.hermano;
        }
        NodoCB hijoActual = min.hijo;
        NodoCB prevHijo = null;
        while (hijoActual != null) {
            NodoCB nextHijo = hijoActual.hermano;
            hijoActual.hermano = prevHijo;
            hijoActual.padre = null; // Romper enlace con el padre extraído
            prevHijo = hijoActual;
            hijoActual = nextHijo;
        }
        ColaBinomial temp = new ColaBinomial();
        temp.head = prevHijo;
        this.unir(temp);
        return min;
    }
    public static int[] K_MergeCB(int [][] arreglo){
        int k=arreglo.length;
        int n=0;
        for(int i=0;i<k;i++){
            n+=arreglo[i].length;
        }
        int[] B=new int[n];
        ColaBinomial temp=new ColaBinomial();
        for(int i=0;i<k;i++){
            temp.insertar(new ColaBinomial.NodoCB(arreglo[i][0],i,0));
        }
        for (int i=0;i<n;i++){
            ColaBinomial.NodoCB minimo=temp.extraerMinimo();
            B[i]=minimo.valor;
            int arregloId=minimo.idArreglo;
            int nextIndice= minimo.indiceElemento+1;
            if (nextIndice<arreglo[arregloId].length){
                temp.insertar(new ColaBinomial.NodoCB(arreglo[arregloId][nextIndice],arregloId,nextIndice));
            }
        }
        return B;
    }
}