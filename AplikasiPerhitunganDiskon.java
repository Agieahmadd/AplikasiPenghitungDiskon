import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import com.formdev.flatlaf.themes.FlatMacLightLaf;

public class AplikasiPerhitunganDiskon extends JFrame {

    private JTextField tfHargaAsli, tfKodeKupon, tfHargaAkhir, tfHemat;
    private JComboBox<String> cbDiskon;
    private JButton btnHitung, btnReset;
    private JSlider sliderDiskon;
    private JTextArea areaRiwayat;
    private JLabel lblResult;
    private ArrayList<String> riwayatList = new ArrayList<>();

    public AplikasiPerhitunganDiskon() {
        // Tema Modern
        FlatMacLightLaf.setup();
        setTitle("💰 Aplikasi Perhitungan Diskon ");
        setSize(550, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // ====== Header ======
        JLabel lblTitle = new JLabel("Aplikasi Perhitungan Diskon", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Poppins", Font.BOLD, 20));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
        lblTitle.setForeground(new Color(25, 25, 60));
        add(lblTitle, BorderLayout.NORTH);

        // ====== Panel Utama ======
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(10, 10, 10, 10);
        c.fill = GridBagConstraints.HORIZONTAL;

        // Input Harga Asli
        c.gridx = 0; c.gridy = 0;
        panel.add(new JLabel("Harga Asli (Rp):"), c);
        tfHargaAsli = new JTextField();
        c.gridx = 1; 
        panel.add(tfHargaAsli, c);

        // Pilihan Diskon
        c.gridx = 0; c.gridy = 1;
        panel.add(new JLabel("Diskon (%):"), c);
        cbDiskon = new JComboBox<>(new String[]{"5", "10", "15", "20", "25", "30", "50"});
        c.gridx = 1; 
        panel.add(cbDiskon, c);

        // Slider Diskon (opsional)
        c.gridy = 2; c.gridx = 0;
        panel.add(new JLabel("Atur Diskon (Slider):"), c);
        sliderDiskon = new JSlider(0, 100, 10);
        sliderDiskon.setMajorTickSpacing(20);
        sliderDiskon.setMinorTickSpacing(5);
        sliderDiskon.setPaintTicks(true);
        sliderDiskon.setPaintLabels(true);
        c.gridx = 1; 
        panel.add(sliderDiskon, c);

        // Kode Kupon
        c.gridy = 3; c.gridx = 0;
        panel.add(new JLabel("Kode Kupon (opsional):"), c);
        tfKodeKupon = new JTextField();
        c.gridx = 1; 
        panel.add(tfKodeKupon, c);

        // Tombol
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        btnHitung = new JButton("Hitung");
        btnHitung.setBackground(new Color(70, 130, 180));
        btnHitung.setForeground(Color.WHITE);
        btnHitung.setFont(new Font("Poppins", Font.BOLD, 14));

        btnReset = new JButton("Reset");
        btnReset.setBackground(new Color(220, 20, 60));
        btnReset.setForeground(Color.WHITE);
        btnReset.setFont(new Font("Poppins", Font.BOLD, 14));
        buttonPanel.add(btnHitung);
        buttonPanel.add(btnReset);

        c.gridy = 4; c.gridx = 0; c.gridwidth = 2;
        panel.add(buttonPanel, c);

        // Hasil
        c.gridwidth = 1;
        c.gridy = 5; c.gridx = 0;
        panel.add(new JLabel("Harga Akhir (Rp):"), c);
        tfHargaAkhir = new JTextField();
        tfHargaAkhir.setEditable(false);
        c.gridx = 1; panel.add(tfHargaAkhir, c);

        c.gridy = 6; c.gridx = 0;
        panel.add(new JLabel("Penghematan (Rp):"), c);
        tfHemat = new JTextField();
        tfHemat.setEditable(false);
        c.gridx = 1; panel.add(tfHemat, c);

        // Riwayat
        c.gridy = 7; c.gridx = 0;
        panel.add(new JLabel("Riwayat Perhitungan:"), c);
        areaRiwayat = new JTextArea(5, 30);
        areaRiwayat.setEditable(false);
        areaRiwayat.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        c.gridx = 1;
        panel.add(new JScrollPane(areaRiwayat), c);

        add(panel, BorderLayout.CENTER);

        // Label hasil animasi
        lblResult = new JLabel("", SwingConstants.CENTER);
        lblResult.setFont(new Font("Poppins", Font.ITALIC, 14));
        lblResult.setForeground(new Color(100, 100, 100));
        lblResult.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 10));
        add(lblResult, BorderLayout.SOUTH);

        // ====== EVENT ======
        btnHitung.addActionListener(e -> hitungDiskon());
        btnReset.addActionListener(e -> resetForm());
        sliderDiskon.addChangeListener((ChangeEvent e) -> {
            cbDiskon.setSelectedItem(String.valueOf(sliderDiskon.getValue()));
        });
    }

    private void hitungDiskon() {
        try {
            double harga = Double.parseDouble(tfHargaAsli.getText());
            double diskon = Double.parseDouble(cbDiskon.getSelectedItem().toString());

            // Cek kode kupon
            String kupon = tfKodeKupon.getText().trim().toUpperCase();
            double tambahan = 0;
            if (kupon.equals("HEMAT10")) tambahan = 10;
            else if (kupon.equals("SUPER5")) tambahan = 5;

            double totalDiskon = diskon + tambahan;
            if (totalDiskon > 100) totalDiskon = 100;

            double hemat = harga * (totalDiskon / 100);
            double hargaAkhir = harga - hemat;

            DecimalFormat df = new DecimalFormat("#,###.00");
            tfHemat.setText(df.format(hemat));
            tfHargaAkhir.setText(df.format(hargaAkhir));

            String hasil = "Diskon " + totalDiskon + "% → Hemat Rp" + df.format(hemat) +
                           " → Harga Akhir Rp" + df.format(hargaAkhir);
            riwayatList.add(hasil);
            updateRiwayat();
            animateResult("✅ Perhitungan berhasil! Total Diskon: " + totalDiskon + "%");

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Masukkan angka yang valid untuk harga!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void resetForm() {
        tfHargaAsli.setText("");
        tfKodeKupon.setText("");
        tfHargaAkhir.setText("");
        tfHemat.setText("");
        areaRiwayat.setText("");
        riwayatList.clear();
        lblResult.setText("");
    }

    private void updateRiwayat() {
        StringBuilder sb = new StringBuilder();
        for (String s : riwayatList) {
            sb.append("- ").append(s).append("\n");
        }
        areaRiwayat.setText(sb.toString());
    }

    private void animateResult(String text) {
        lblResult.setText("");
        Timer timer = new Timer(25, null);
        final int[] index = {0};
        timer.addActionListener(e -> {
            if (index[0] < text.length()) {
                lblResult.setText(text.substring(0, index[0] + 1));
                index[0]++;
            } else ((Timer) e.getSource()).stop();
        });
        timer.start();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AplikasiPerhitunganDiskon().setVisible(true));
    }
}
