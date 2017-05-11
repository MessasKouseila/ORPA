package graphic;

import java.util.Random;

import instance.Instance;

public class Tes {

	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		Instance ins = new Instance(6);
		/*
		Random r = new Random();
		for (int i = 0; i < 6; ++i) {
			System.out.println(r.nextBoolean());
			Thread.sleep(1000);
		}
		*/
		for (int i = 0; i < 6; ++i) {
			for (int j = 0; j < 6; ++j) {
				System.out.print(ins.lien[i][j]);
				System.out.print(" ");
			}
			System.out.println("\n");
		}

	}

}
