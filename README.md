# JANJI
Saya Khalifa Nabil Nur dengan NIM 2511372 mengerjakan Tugas Praktikum 5 dalam mata kuliah Desain dan Pemrograman Berorientasi Objek untuk keberkahanNya maka saya tidak melakukan kecurangan seperti yang telah dispesifikasikan. Aamiin.

## Desain Program
Atribut data : 
1. **ID** (`VARCHAR` - Primary Key): Nomor identitas unik (misal: NIM, NIP, ID Pegawai).
2. **Nama** (`VARCHAR`): Nama lengkap person.
3. **Tahun Lahir** (`INT`): Tahun kelahiran.
4. **Kategori** (`VARCHAR`): Peran/posisi (Mahasiswa, Dosen, Tenaga Pendidik, OB).
5. **Jenis Kelamin** (`VARCHAR` - Component Bonus JRadioButton): Pilihan `Laki-Laki` atau `Perempuan`.

Komponen GUI :
- **`JTextField`**: Digunakan untuk input `ID`, `Nama`, dan `Tahun Lahir`.
- **`JComboBox`**: Digunakan untuk memilih `Kategori`.
- **`JRadioButton`**: Digunakan untuk memilih `Jenis Kelamin`.
- **`JButton`**: Terdiri dari tombol **Add/Update**, **Cancel**, dan **Delete**.
- **`JTable` & `JScrollPane`**: Menampilkan daftar data yang diambil langsung dari database MySQL.


## Penjelasan Alur Kerja Program
1. **Inisialisasi & Pembacaan Data (Read)**
   - Saat aplikasi dijalankan, method `PeopleMenu()` akan menginisialisasi koneksi ke database MySQL via kelas `Database`.
   - Method `setTable()` dieksekusi untuk menjalankan query `SELECT * FROM person`.
   - Data dari database dibaca menggunakan `ResultSet` dan dimuat ke dalam `JTable`.

2. **Pengisian / Pemilihan Data (Event Handling JTable)**
   - Ketika pengguna mengklik salah satu baris pada tabel, `MouseListener` mengambil nilai dari baris yang dipilih dan memasukkannya ke dalam form input.
   - Teks pada tombol `Add` berubah menjadi `Update`, dan tombol `Delete` ditampilkan.

3. **Penambahan Data Baru (Create)**
   - Saat tombol **Add** ditekan:
     - **Validasi 1**: Memeriksa apakah ada kolom input yang kosong. Jika ada, muncul pop-up error.
     - **Validasi 2**: Memeriksa apakah ID sudah terdaftar di database via `isIdExists()`. Jika duplikat, muncul pop-up error.
     - Jika lolos validasi, query `INSERT INTO person ...` dieksekusi dan tabel diperbarui secara otomatis.

4. **Pengubahan Data (Update)**
   - Saat baris data dipilih dan pengguna menekan tombol **Update**:
     - Program memvalidasi kelengkapan form input.
     - Query `UPDATE person SET ... WHERE id = '...'` dieksekusi untuk memperbarui record di database.

5. **Penghapusan Data (Delete)**
   - Saat baris data dipilih dan pengguna menekan tombol **Delete**:
     - Program menampilkan dialog konfirmasi (`JOptionPane.showConfirmDialog`).
     - Jika pengguna memilih **YES**, query `DELETE FROM person WHERE id = '...'` dieksekusi.

6. **Reset Form (Cancel)**
   - Mengosongkan seluruh isi field input, mengembalikan tombol ke status **Add**, dan menyembunyikan tombol **Delete**.
