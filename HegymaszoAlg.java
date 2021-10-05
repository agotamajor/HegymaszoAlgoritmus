import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

public class HegymaszoAlg {
    public static Integer legkozelebbi(int x, int y, Map<Integer, Koordinatak> graf, boolean[] megNincs) {
        double min = 10000000;
        Integer minInd = 0;

        for (Map.Entry<Integer, Koordinatak> entry: graf.entrySet()) {
            double tav = Math.sqrt(Math.pow(x - entry.getValue().getX(), 2) + Math.pow(y - entry.getValue().getY(), 2));
            Integer key = entry.getKey();

            if (megNincs[key - 1] && tav < min) {
                min = tav;
                minInd = key;
            }
        }

        return minInd;
    }

    public static double korHossza(Integer key, Map<Integer, Koordinatak> graf, boolean[] megNincs, Integer[] kor) {
        int n = graf.size();
        for (int i = 0; i < n; i++) {
            megNincs[i] = true;
        }

        kor[0] = key;
        megNincs[key - 1] = false;
        double tav = 0;
        int x, y;

        for (int i = 1; i < n; i++) {
            x = graf.get(kor[i-1]).getX();
            y = graf.get(kor[i-1]).getY();
            kor[i] = legkozelebbi(x, y, graf, megNincs);
            megNincs[kor[i] - 1] = false;
            tav = tav + Math.sqrt(Math.pow(x - graf.get(kor[i]).getX(), 2) + Math.pow(y - graf.get(kor[i]).getY(), 2));
        }

        return tav + Math.sqrt(Math.pow(graf.get(kor[n-1]).getX() - graf.get(key).getX(), 2) + Math.pow(graf.get(kor[n-1]).getY() - graf.get(key).getY(), 2));
    }

    public static double legrovidebbKor(Map<Integer, Koordinatak> graf, boolean[] megNincs, Integer[] minKor) {
        double minTav = 100000;
        int n = graf.size();
        Integer[] kor = new Integer[n];

        for (Map.Entry<Integer, Koordinatak> entry: graf.entrySet()) {
            double tav = korHossza(entry.getKey(), graf, megNincs, kor);

            if (tav < minTav) {
                minTav = tav;
                for (int i = 0; i < n; i++) {
                    minKor[i] = kor[i];
                }
            }
        }

        return minTav;
    }

    public static void main(String[] args) throws FileNotFoundException {
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

        int n = graf.size();
        boolean[] megNincs = new boolean[n];
        Integer[] minKor = new Integer[n];

        System.out.println(legrovidebbKor(graf, megNincs, minKor));
    }
}
