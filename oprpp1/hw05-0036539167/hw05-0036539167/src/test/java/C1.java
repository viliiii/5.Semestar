public class C1 {
    private static int a = 0;
    private int b;
    public C1(int b) {
        this.b = b;
        a++;
    }

    public static void main(String[] args) {
        int[] arr1 = {1, 2, 3, 4};
        C1[] arr2 = {
                new C1(5),
                new C1(7)
        };
        C1 c = arr2[0];
        c.b = 3;


        System.out.println(a);
    }
}

