public class Main {
    public static void main(String[] args) {
String today = "2024-07-01";

        String[] mangaList = {"One Piece", "Naruto", "Bleach"};
        String[] releaseDates = {"2024-07-01", "2024-08-15", "2024-09-10"};

        for(int i = 0; i < mangaList.length; i++) {
            checkReleaseDate(today, mangaList[i], releaseDates[i]);
        }
    }

    static void checkReleaseDate(String today, String manga, String releaseDate){
        if(today.equals(releaseDate)){
                System.out.println(manga + "は今日が発売日!");
            }else {
                System.out.println(manga + "はまだ発売日ではありません");
            }

    }
}
