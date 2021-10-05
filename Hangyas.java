import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Hangyas {
    public static double max(Elek[] p, int n, double kisebb) {
        double max = -1000000;
        for (int i = 0; i < n; i++) {
            if (p[i].getErtek() > max && p[i].getErtek() < kisebb) {
                max = p[i].getErtek();
            }
        }
        return max;
    }

    public static double minoseg(Integer[] S, int n, Map<Integer, Koordinatak> graf) {
        double MinosegS = 0;
        for (int l = 1; l < n; l++) {
            // kiszamolni az S tomben tarolt pontok kozti euklideszi tavolsagot
            MinosegS += Math.sqrt(Math.pow(graf.get(S[l - 1]).getX() - graf.get(S[l]).getX(), 2) + Math.pow(graf.get(S[l - 1]).getY() - graf.get(S[l]).getY(), 2));
        }
        MinosegS += Math.sqrt(Math.pow(graf.get(S[n - 1]).getX() - graf.get(S[0]).getX(), 2) + Math.pow(graf.get(S[n - 1]).getY() - graf.get(S[0]).getY(), 2));
        return MinosegS;
    }

    public static void main(String[] args) throws FileNotFoundException {
        Random rand = new Random();
        File read = new File("Teszt1.tsp");
        Scanner reader = new Scanner(read);

        Map<Integer, Koordinatak> graf = new TreeMap<Integer, Koordinatak>();

        while (reader.hasNextLine()) {
            Integer key;
            int x, y;
            key = reader.nextInt();
            x = reader.nextInt();
            y = reader.nextInt();
            graf.put(key, new Koordinatak(x, y));
        }

        int PopulacioMeret = 10;
        int n = graf.size();
        double e = 0.05;       //parologtatasi egyutthato
        double[][] p = new double[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                p[i][j] = 10000;
            }
            p[i][i] = 0;
        }

        double Legjobb = 0;
        Integer[] S = new Integer[n];
        boolean[] megNincs = new boolean[n];
        Koordinatak[] C = new Koordinatak[n];
        int szamlalo = 0;

        while (szamlalo < 20) {
            szamlalo++;
            Collection<Integer[]> P = new ArrayList<>();

            for (int j = 0; j < PopulacioMeret; j++) {
                for (int l = 0; l < n; l++) {
                    S[l] = 0;
                }
                for (int l = 0; l < n; l++) {
                    megNincs[l] = true;
                }
                int kezd = rand.nextInt(n) + 1;
                S[0] = kezd;
                megNincs[kezd - 1] = false;

                for (int k = 1; k < n; k++) {
                    int index = 0;
                    Elek[] elek = new Elek[n - k + 1];
                    for (int l = 0; l < n; l++) {
                        if (megNincs[l]) {
                            elek[index] = new Elek(l + 1, p[S[k - 1] - 1][l] / Math.sqrt(Math.pow(graf.get(S[k - 1]).getX() - graf.get(l + 1).getX(), 2) + Math.pow(graf.get(S[k - 1]).getY() - graf.get(l + 1).getY(), 2)));
                            index++;
                        }
                    }
                    double maxP = max(elek, index, Double.POSITIVE_INFINITY);
                    int m = index;
                    index = 0;
                    while (index == 0) {
                        for (int l = 0; l < m; l++) {
                            if (elek[l].getErtek() == maxP && megNincs[elek[l].getCsp() - 1]) {
                                C[index] = new Koordinatak(S[k], elek[l].getCsp());
                                index++;
                            }
                        }
                        maxP = max(elek, m, maxP);
                    }

                    int r = rand.nextInt(index);
                    S[k] = C[r].getY();
                    megNincs[S[k] - 1] = false;
                }

                double MinosegS = minoseg(S, n, graf);
                if (Legjobb == 0 || MinosegS < Legjobb) {
                    Legjobb = MinosegS;
                }

                P.add(S);
            }

            for (int j = 0; j < n; j++) {
                for (int l = 0; l < n; l++) {
                    p[j][l] *= (1 - e);
                }
            }

            Iterator<Integer[]> iterator = P.iterator();
            while (iterator.hasNext()) {
                Integer[] P_ = iterator.next();

                for (int j = 1; j < n; j++) {
                    p[P_[j - 1] - 1][P_[j] - 1] += 1 / minoseg(P_, n, graf);
                }
            }
        }

        System.out.println(Legjobb);
    }
}
