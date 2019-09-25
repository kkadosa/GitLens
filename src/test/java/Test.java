import java.util.stream.IntStream;

public class Test {
    public static void main(String[] args) {
        IntStream.range(0, 100).parallel().forEach(i -> {
            if(i == 99){
                try {
                    Thread.sleep(6000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println(i + " " + Thread.currentThread().getId());
        });
        System.out.println("d" + Thread.currentThread().getId());
    }
}
