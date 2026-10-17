import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PeopleMenu extends JFrame {
    // Komponen GUI
    public JPanel mainPanel;
    private JTextField idField;
    private JTextField namaField;
    private JTextField tahunLahirField;
    private JComboBox<String> kategoriComboBox;

    // RadioButton untuk Jenis Kelamin
    private JRadioButton lakiLakiRadioButton;
    private JRadioButton perempuanRadioButton;
    private ButtonGroup jenisKelaminGroup;

    private JButton addUpdateButton;
    private JButton cancelButton;
    private JButton deleteButton;
    private JTable peopleTable;
    private JLabel titleLabel;

    // Objek Database & Tracker
    private Database database;
    private int selectedIndex = -1;
    private String originalSelectedId = ""; // Menyimpan ID asli sebelum diedit saat update

    public PeopleMenu() {
        // Inisialisasi Koneksi Database
        database = new Database();

        // Grouping RadioButton Jenis Kelamin
        jenisKelaminGroup = new ButtonGroup();
        jenisKelaminGroup.add(lakiLakiRadioButton);
        jenisKelaminGroup.add(perempuanRadioButton);

        // Header Title Style
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 20f));

        // Pilihan ComboBox Kategori disesuaikan dengan data dummy
        String[] kategoriData = {
                "--- Pilih Kategori ---",
                "Mahasiswa",
                "Dosen",
                "Tenaga Pendidik",
                "OB"
        };
        kategoriComboBox.setModel(new DefaultComboBoxModel<>(kategoriData));

        // Load data awal dari database ke tabel
        peopleTable.setModel(setTable());

        // Sembunyikan tombol delete di awal
        deleteButton.setVisible(false);

        // Listener: Tombol Add / Update
        addUpdateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedIndex == -1) {
                    insertData();
                } else {
                    updateData();
                }
            }
        });

        // Listener: Tombol Delete (dengan Confirmation Prompt)
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int option = JOptionPane.showConfirmDialog(
                        null,
                        "Apakah Anda yakin ingin menghapus data ini?",
                        "Konfirmasi Hapus",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE
                );

                if (option == JOptionPane.YES_OPTION) {
                    deleteData();
                }
            }
        });

        // Listener: Tombol Cancel
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearForm();
            }
        });

        // Listener: Klik Baris Tabel
        peopleTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                selectedIndex = peopleTable.getSelectedRow();

                String curId = peopleTable.getModel().getValueAt(selectedIndex, 0).toString();
                String curNama = peopleTable.getModel().getValueAt(selectedIndex, 1).toString();
                String curTahun = peopleTable.getModel().getValueAt(selectedIndex, 2).toString();
                String curKategori = peopleTable.getModel().getValueAt(selectedIndex, 3).toString();
                String curJenisKelamin = peopleTable.getModel().getValueAt(selectedIndex, 4).toString();

                // Simpan ID asli baris yang diklik untuk acuan klausa WHERE saat Update
                originalSelectedId = curId;

                // Set data ke form (ID tetap bisa diketik/diedit)
                idField.setText(curId);
                namaField.setText(curNama);
                tahunLahirField.setText(curTahun);
                kategoriComboBox.setSelectedItem(curKategori);

                if (curJenisKelamin.equalsIgnoreCase("Laki-Laki")) {
                    lakiLakiRadioButton.setSelected(true);
                } else if (curJenisKelamin.equalsIgnoreCase("Perempuan")) {
                    perempuanRadioButton.setSelected(true);
                } else {
                    jenisKelaminGroup.clearSelection();
                }

                addUpdateButton.setText("Update");
                deleteButton.setVisible(true);
            }
        });
    }

    // Helper: Mengambil nilai Jenis Kelamin dari RadioButton
    private String getSelectedJenisKelamin() {
        if (lakiLakiRadioButton.isSelected()) return "Laki-Laki";
        if (perempuanRadioButton.isSelected()) return "Perempuan";
        return "";
    }

    // Read: Menampilkan Data dari Database ke Table
    public final DefaultTableModel setTable() {
        Object[] cols = {"ID", "Nama", "Tahun Lahir", "Kategori", "Jenis Kelamin"};
        DefaultTableModel tmp = new DefaultTableModel(null, cols);

        try {
            ResultSet resultSet = database.selectQuery("SELECT * FROM person");
            while (resultSet.next()) {
                Object[] row = new Object[5];
                row[0] = resultSet.getString("id");
                row[1] = resultSet.getString("nama");
                row[2] = resultSet.getInt("thn_lahir");
                row[3] = resultSet.getString("kategori");
                row[4] = resultSet.getString("jenis_kelamin");
                tmp.addRow(row);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return tmp;
    }

    // Helper: Memeriksa apakah ID sudah terdaftar
    private boolean isIdExists(String id) {
        try {
            ResultSet rs = database.selectQuery("SELECT id FROM person WHERE id = '" + id + "'");
            return rs.next();
        } catch (SQLException e) {
            return false;
        }
    }

    // Create: Insert Data Baru
    public void insertData() {
        String id = idField.getText().trim();
        String nama = namaField.getText().trim();
        String tahunStr = tahunLahirField.getText().trim();
        String kategori = kategoriComboBox.getSelectedItem().toString();
        String jenisKelamin = getSelectedJenisKelamin();

        // Validasi 1: Kolom Input Kosong
        if (id.isEmpty() || nama.isEmpty() || tahunStr.isEmpty() ||
                kategori.equals("--- Pilih Kategori ---") || jenisKelamin.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Semua kolom input harus diisi!", "Error Input", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validasi 2: ID Duplikat Saat Insert
        if (isIdExists(id)) {
            JOptionPane.showMessageDialog(null, "ID '" + id + "' sudah ada di database!", "Error ID Duplikat", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int tahun = Integer.parseInt(tahunStr);

            String sql = String.format(
                    "INSERT INTO person VALUES ('%s', '%s', %d, '%s', '%s')",
                    id, nama, tahun, kategori, jenisKelamin
            );
            database.insertUpdateDeleteQuery(sql);

            peopleTable.setModel(setTable());
            clearForm();

            JOptionPane.showMessageDialog(null, "Data berhasil ditambahkan!");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Tahun Lahir harus berupa angka!", "Error Format", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Update: Mengubah Data
    public void updateData() {
        String newId = idField.getText().trim();
        String nama = namaField.getText().trim();
        String tahunStr = tahunLahirField.getText().trim();
        String kategori = kategoriComboBox.getSelectedItem().toString();
        String jenisKelamin = getSelectedJenisKelamin();

        // Validasi 1: Kolom Input Kosong
        if (newId.isEmpty() || nama.isEmpty() || tahunStr.isEmpty() ||
                kategori.equals("--- Pilih Kategori ---") || jenisKelamin.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Semua kolom input harus diisi!", "Error Input", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validasi 2: ID Duplikat Saat Update
        // Jika ID diubah dan ID baru tersebut ternyata sudah ada di database milik data orang lain
        if (!newId.equals(originalSelectedId) && isIdExists(newId)) {
            JOptionPane.showMessageDialog(null, "ID '" + newId + "' sudah digunakan oleh data lain!", "Error ID Duplikat", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int tahun = Integer.parseInt(tahunStr);

            // Update data termasuk memperbarui ID (jika diubah) berpatokan pada originalSelectedId
            String sql = String.format(
                    "UPDATE person SET id='%s', nama='%s', thn_lahir=%d, kategori='%s', jenis_kelamin='%s' WHERE id='%s'",
                    newId, nama, tahun, kategori, jenisKelamin, originalSelectedId
            );
            database.insertUpdateDeleteQuery(sql);

            peopleTable.setModel(setTable());
            clearForm();

            JOptionPane.showMessageDialog(null, "Data berhasil diubah!");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Tahun Lahir harus berupa angka!", "Error Format", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Delete: Menghapus Data
    public void deleteData() {
        String id = originalSelectedId;

        String sql = "DELETE FROM person WHERE id='" + id + "'";
        database.insertUpdateDeleteQuery(sql);

        peopleTable.setModel(setTable());
        clearForm();

        JOptionPane.showMessageDialog(null, "Data berhasil dihapus!");
    }

    // Clear Form & Reset State
    public void clearForm() {
        idField.setText("");
        namaField.setText("");
        tahunLahirField.setText("");
        kategoriComboBox.setSelectedIndex(0);
        jenisKelaminGroup.clearSelection();

        addUpdateButton.setText("Add");
        deleteButton.setVisible(false);
        selectedIndex = -1;
        originalSelectedId = "";
    }

    // Main Method
    public static void main(String[] args) {
        PeopleMenu menu = new PeopleMenu();
        menu.setSize(750, 600);
        menu.setLocationRelativeTo(null);
        menu.setContentPane(menu.mainPanel);
        menu.getContentPane().setBackground(Color.WHITE);
        menu.setVisible(true);
        menu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}