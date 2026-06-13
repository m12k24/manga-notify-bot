public class Main {
    public static void main(String[] args) {
        String today = "2026-06-13";

        String[] mangaList = {"ワンピース", "ナルト", "鬼滅の刃"};
        String[] releaseDates = {"2026-06-13", "2026-07-01", "2026-06-13"};

        for (int i = 0; i < mangaList.length; i++) {
            checkReleaseDate(today, mangaList[i], releaseDates[i]);
        }
    }

    static void checkReleaseDate(String today, String manga, String releaseDate) {
        if (today.equals(releaseDate)) {
            System.out.println(manga + "は今日が発売日！");
        } else {
            System.out.println(manga + "はまだ発売日ではありません");
        }
    }
}