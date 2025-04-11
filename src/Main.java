import java.util.Scanner;


public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== Kompresi Gambar dengan Quadtree ===");
        System.out.print("Masukkan path absolut ke file gambar (misal: C:/images/foto.jpg): ");
        String inputPath = scanner.nextLine().trim();

        System.out.print("Masukkan path absolut output tanpa ekstensi (misal: C:/images/output): ");
        String outputPath = scanner.nextLine().trim();

        System.out.println("Pilih metric:");
        System.out.println("1: Variance");
        System.out.println("2: Mean Absolute Deviation (MAD)");
        System.out.println("3: Max Pixel Difference");
        System.out.println("4: Entropy");
        System.out.print("Masukkan nomor metric (1-4): ");
        int metricNum = 0;
        while (true) {
            if (scanner.hasNextInt()) {
                metricNum = scanner.nextInt();
                if (metricNum >= 1 && metricNum <= 4) {
                    break;
                }
            }
            System.out.println("Nomor metric tidak valid. Harus antara 1 - 4.");
            scanner.nextLine();
        }

        double threshold = -1;
        while (true) {
            System.out.print("Masukkan error threshold (misal: 500.0): ");
            if (scanner.hasNextDouble()) {
                threshold = scanner.nextDouble();
                boolean valid = false;

                switch (metricNum) {
                    case 1: 
                        valid = threshold >= 0;
                        if (!valid) System.out.println("Threshold untuk Variance harus >= 0.");
                        break;
                    case 2: 
                        valid = threshold >= 0 && threshold <= 255;
                        if (!valid) System.out.println("Threshold untuk MAD harus antara 0 - 255.");
                        break;
                    case 3: 
                        valid = threshold >= 0 && threshold <= 255;
                        if (!valid) System.out.println("Threshold untuk Max Pixel Difference harus antara 0 - 255.");
                        break;
                    case 4:
                        valid = threshold >= 1 && threshold <= 8;
                        if (!valid) System.out.println("Threshold untuk Entropy harus antara 1 - 8.");
                        break;
                }
                if (valid){
                    break;
                } 
            } else {
                scanner.next(); 
            }

            System.out.println("Input tidak valid. Coba lagi.");
        }

        int minBlockSize = 0;
        while (true) {
            System.out.print("Masukkan minimum block size (misal: 4): ");
            if (scanner.hasNextInt()) {
                minBlockSize = scanner.nextInt();
                if (minBlockSize >= 1) {
                    break;
                }
            }
            System.out.println("Block size tidak valid. Masukkan angka >= 1.");
            scanner.nextLine(); 
        }
        scanner.close();

        Image image = new Image(inputPath);
        if (!image.isFailToLoad()) {
            Quadtree.EROR_TRESHOLD=threshold;       
            Quadtree.MIN_SIZE = minBlockSize;      
            System.err.println("Processing . . .");
            System.out.println();
            long startTime = System.currentTimeMillis();
            image.compressImage(outputPath, metricNum, threshold);
            long endTime = System.currentTimeMillis();
            
            long duration = endTime - startTime;
            System.out.println("Waktu eksekusi: " + duration + " ms");

        } else {
            System.out.println("Gagal memuat gambar. Program dihentikan.");
        }
    }

}

// public class Main {
//     public static void main(String[] args) {
//         // Path file gambar (ganti dengan nama filemu)
//     String inputImagePath = "input5.jpg"; // pastikan file ini ada di direktori project
//         String outputName = "hasil_kompres";

//         int metric = 1; // 1: Variance, 2: MAD, 3: MaxPixelDiff, 4: Entropy
//         double errorThreshold = 80; 
//         int minSize = 30; // Minimum luas area untuk dipecah (contoh: 64 piksel)

//         // // Set parameter
//         // int metric = 2; // 1: Variance, 2: MAD, 3: MaxPixelDiff, 4: Entropy
//         // double errorThreshold = 5; 
//         // int minSize = 4; // Minimum luas area untuk dipecah (contoh: 64 piksel)

//         // int metric = 3; // 1: Variance, 2: MAD, 3: MaxPixelDiff, 4: Entropy
//         // double errorThreshold = 40; 
//         // int minSize = 30; // Minimum luas area untuk dipecah (contoh: 64 piksel)

//         // int metric = 4; // 1: Variance, 2: MAD, 3: MaxPixelDiff, 4: Entropy
//         // double errorThreshold = 1.5; 
//         // int minSize = 8; // Minimum luas area untuk dipecah (contoh: 64 piksel)

//         // Set konfigurasi global Quadtree
//         Quadtree.MIN_SIZE = minSize;
//         Quadtree.EROR_TRESHOLD = errorThreshold;
//         Quadtree.jumlahObjek = 0;

//         // // Buat dan kompres gambar
//         Image gambar = new Image(inputImagePath);
//         // Quadtree root = new Quadtree(gambar.getWidth(), gambar.getHeight(), 0, 0, gambar.getOriginal(), metric);
//         // Map<Integer, List<Quadtree>> stepsMap = new LinkedHashMap<>();
//         // root.subDivide(0, stepsMap);

//         // for (Map.Entry<Integer, List<Quadtree>> entry : stepsMap.entrySet()) {
//         //     int step = entry.getKey();
//         //     int count = entry.getValue().size();
//         //     System.out.println("Step " + step + ": " + count + " sut");
//         // }





//         // // Mengambil node-node pada langkah ke-8
//         // List<Quadtree> step8Nodes = stepsMap.get(3);

//         // // Membuat canvas baru dengan ukuran gambar yang sesuai
//         // BufferedImage canvas = new BufferedImage(gambar.getWidth(), gambar.getHeight(), gambar.getOriginal().getType());

//         // // Memastikan bahwa ada node pada langkah ke-8
//         // if (step8Nodes != null) {
//         //     // Menggambar semua node pada langkah ke-8 ke canvas
//         //     for (Quadtree node : step8Nodes) {
//         //         node.drawToCanvas(canvas); // Pastikan drawToCanvas menerima Graphics2D
//         //         System.out.println(node.getCompressedColor());
//         //     }
//         //     try{
//         //         File output = new File("cobastep3" + ".png");
//         //         ImageIO.write(canvas, "png", output);
//         //     } catch(IOException e) {
//         //         System.err.println("Gagal menyimpan gambar: " + e.getMessage());
//         //     }


//         // } else {
//         //     System.out.println("Node pada langkah ke-8 tidak ditemukan.");
//         // }


//         gambar.compressImage(outputName, metric, errorThreshold);
//     }
// }

