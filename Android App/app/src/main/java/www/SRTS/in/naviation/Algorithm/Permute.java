package www.SRTS.in.naviation.Algorithm;

public class Permute {
    int Permutations[][];
    int ctr=0;
    Permute(int[] arr){
        Permutations = new int[f(arr.length)][arr.length];
        permuteHelper(arr,0,arr.length-1);
    }
    int f(int num){
        if(num<=1)return 1;
        return num*f(num-1);
    }


    private void permuteHelper(int[] arr, int l,int r){

        if(r==l){
            Permutations[ctr++]=arr.clone();


        }
        else{
            for(int i=l;i<=r;i++) {
                arr=swap(arr,l,i);
                permuteHelper(arr,l+1,r);
                arr=swap(arr,l,i);
            }
        }
    }

    private int[] swap(int[] arr, int l, int i) {
        int temp = arr[l];
        arr[l]=arr[i];
        arr[i]=temp;
        return arr;
    }

    public void PrintArray(){
        for(int i=0;i<Permutations.length;i++){
            for(int j=0;j<Permutations[0].length;j++){
                System.out.print(Permutations[i][j]);
                if(j!=Permutations[0].length-1)System.out.print(",");
            }
            System.out.print("\n");
        }
    }
}
