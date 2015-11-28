package Test;

import java.util.Scanner;


public class Home {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int k=0, max=0, diemMax=0;
		int loaiDiem[]= new int[11];
		Scanner input = new Scanner(System.in);
		System.out.print("Nhap so luong sinh vien: ");
		int N = input.nextInt();
		
		String tensv [] = new String[N];
		String mssv [] = new String[N];
		int diem [] = new int[N];
		
		//Nhập dữ liệu cho sinh viên
		System.out.print("Nhap du lieu cho sinh vien!!! ");
		System.out.println("(Ho va ten viet lien khong dau)");
		for (int i=0;i<N;i++){
			int stt=i+1;
			System.out.println("Sinh vien " +stt );
			System.out.print("Ho va ten: ");
			input.nextLine();
			tensv[i]=input.nextLine();
			System.out.print("MSSV: ");
			mssv[i]=input.nextLine();
			System.out.print("Diem: ");
			diem[i]=input.nextInt();
			loaiDiem[diem[i]]++;
		}
		System.out.println("******************************************************");
		
		//Hiển thị bảng điểm sinh viên
		System.out.println("Bang diem sinh vien");
		System.out.println("+--------------------+-----------------+");
		System.out.println("|      Ho va ten     |   MSSV   | Diem |");
		System.out.println("+--------------------+-----------------+");
		for (int i=0;i<N;i++){
			String space="";
			int dai1 = 19 - tensv[i].length();
			for (int j=0;j<dai1;j++) space +=" ";
			System.out.print("| "+tensv[i]+space+"|");
			space ="";
			int dai2 = 9 - mssv[i].length();
			for (int j=0;j<dai2;j++) space +=" ";
			if(diem[i]<10)
			System.out.println(" "+mssv[i]+space+"| "+diem[i]+"    |");
			else System.out.println(" "+mssv[i]+space+"| "+diem[i]+"   |");
			System.out.println("+--------------------+-----------------+");
		}
		System.out.println("*******************************************************");
		
		//Tìm kiếm theo tên sinh viên
		System.out.print("Nhap ten sinh vien muon tim kiem: ");
		String timkiem = input.next();
		for (int i=0;i<N;i++){
			int ss=tensv[i].compareTo (timkiem);
			if(ss==0)
				System.out.println("Diem cua sinh vien '"+tensv[i]+"' la: "+diem[i]);
			else k++;
			if (k==N)
				System.out.println("Khong tim thay sinh vien");	
		}
		
		//Kiểm tra loại điểm có nhiều sinh viên đạt được nhất
		for (int i=0;i<10;i++)
			if (loaiDiem[i]>max) {max=loaiDiem[i];diemMax=i;}
		System.out.println("*******************************************************");
		
		//Hiển thị điểm có nhiều sinh viên đạt được nhất và danh sách các sinh viên đó
		System.out.println("Diem co nhieu sinh vien dat duoc nhat: " +diemMax);
		System.out.println("+--------------------+-----------------+");
		System.out.println("|      Ho va ten     |   MSSV   | Diem |");
		System.out.println("+--------------------+-----------------+");
		for (int i=0;i<N;i++){
			if (diem[i]==diemMax) {
				String space="";
				int dai1 = 19 - tensv[i].length();
				for (int j=0;j<dai1;j++) space +=" ";
				System.out.print("| "+tensv[i]+space+"|");
				space ="";
				int dai2 = 9 - mssv[i].length();
				for (int j=0;j<dai2;j++) space +=" ";
				if(diem[i]<10)
				System.out.println(" "+mssv[i]+space+"| "+diem[i]+"    |");
				else System.out.println(" "+mssv[i]+space+"| "+diem[i]+"   |");
				System.out.println("+--------------------+-----------------+");
			}
			}
		}
}
