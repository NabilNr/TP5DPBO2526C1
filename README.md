# JANJI
Saya Khalifa Nabil Nur dengan NIM 2511372 mengerjakan Tugas Praktikum 5 dalam mata kuliah Desain dan Pemrograman Berorientasi Objek untuk keberkahanNya maka saya tidak melakukan kecurangan seperti yang telah dispesifikasikan. Aamiin.

# Desain Program
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
<br>

# Penjelasan Alur Kerja Program
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
<br>

# Dokumentasi
## NOTE  
- Ketika data sudah di insert akan ada "Data Berhasil Ditambahkan", tetapi ketika di record tidak ada/kedetect
- Ketika data di update akan ada "Data Berhasil Diubah", tetapi ketika di record tidak ada/kedetect
<br>

## READ
<img width="1920" height="520" alt="image" src="https://github.com/user-attachments/assets/dd2796f8-520e-456a-befa-8331593a3bff" />

## ADD 
### Insert
https://github.com/user-attachments/assets/5df44ad3-4718-42d9-8ba5-60a74b966fc7
### Insert Error Jika Ada ID Yang Sama
<img width="1920" height="611" alt="InsertID" src="https://github.com/user-attachments/assets/e99a8787-8fd9-4405-a566-941a8d597dbb" />

### Insert Jika Ada Input Yang Kosong
<img width="1920" height="641" alt="image" src="https://github.com/user-attachments/assets/d2e704d4-7960-47f1-8f94-1baa17e77e2b" />

## UPDATE
### Update
https://github.com/user-attachments/assets/8af6031a-44b2-485d-8311-9bb0b00a4e9f
### Update Jika Ada ID Yang Sama
<img width="1920" height="646" alt="image" src="https://github.com/user-attachments/assets/61e85a8a-b98d-42e8-9a8c-2089c1a4df4e" />

### Update Jika Ada Input Yang Kosong
<img width="1920" height="614" alt="image" src="https://github.com/user-attachments/assets/0eb35afb-0ded-4914-9e47-0089190f22f5" />

## DELETE
<img width="1920" height="616" alt="image" src="https://github.com/user-attachments/assets/13e2cf18-b0ae-480a-8b23-852adec1a0a2" />
<img width="1920" height="485" alt="image" src="https://github.com/user-attachments/assets/8e0dc12c-2865-4ddc-93e7-9c404cb524b8" />








