package com.inventry.managment;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.Vector;
import java.text.SimpleDateFormat;
import javax.swing.RowSorter.SortKey;
import javax.swing.SortOrder;
import java.util.ArrayList;

public class InventoryApp extends JFrame {
    private JTextField pnameField, priceField, quantityField, regDateField, searchField;
    private JTextField locationField, detailLocationField;
    private JButton searchButton;
    private DefaultTableModel tableModel;
    private JTable table;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private TableRowSorter<DefaultTableModel> sorter;

    public InventoryApp() {
        setTitle("K-WATER 결고 관리 시스템");
        setSize(1000, 625);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null); // Center the window

        // Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Product Name Field
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("모델명:"), gbc);
        pnameField = new JTextField(20);
        gbc.gridx = 1;
        formPanel.add(pnameField, gbc);

        // Price Field
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("단가:"), gbc);
        priceField = new JTextField(20);
        gbc.gridx = 1;
        formPanel.add(priceField, gbc);

        // Quantity Field
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("수량:"), gbc);
        quantityField = new JTextField(20);
        gbc.gridx = 1;
        formPanel.add(quantityField, gbc);

        // Location Fields
        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(new JLabel("재고 장소:"), gbc);
        locationField = new JTextField(20);
        gbc.gridx = 1;
        formPanel.add(locationField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(new JLabel("세부 장소:"), gbc);
        detailLocationField = new JTextField(20);
        gbc.gridx = 1;
        formPanel.add(detailLocationField, gbc);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

        JButton addButton = new JButton("모델 추가하기");
        buttonPanel.add(addButton);

        searchButton = new JButton("모델 검색하기");
        buttonPanel.add(searchButton);

        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        formPanel.add(buttonPanel, gbc);

        add(formPanel, BorderLayout.NORTH);

        // Table Panel
        tableModel = new DefaultTableModel(new String[]{"ID", "모델명", "단가", "수량", "업데이트 일자", "장소", "세별 장소"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // 테이블의 셀을 수정할 수 없도록 설정
            }
        };
        table = new JTable(tableModel);

        // 테이블 정렬 기능 추가
        sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);

        table.setFillsViewportHeight(true);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // 정렬 버튼 패널 추가
        JPanel sortButtonPanel = new JPanel();
        sortButtonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        JButton sortAscButton = new JButton("오름차순 정렬");
        JButton sortDescButton = new JButton("내림차순 정렬");

        sortButtonPanel.add(sortAscButton);
        sortButtonPanel.add(sortDescButton);

        add(sortButtonPanel, BorderLayout.SOUTH);

        // 정렬 버튼 액션 리스너 추가
        sortAscButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<SortKey> sortKeys = new ArrayList<>();
                sortKeys.add(new SortKey(1, SortOrder.ASCENDING)); // 모델명 컬럼 오름차순 정렬
                sorter.setSortKeys(sortKeys);
            }
        });

        sortDescButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<SortKey> sortKeys = new ArrayList<>();
                sortKeys.add(new SortKey(1, SortOrder.DESCENDING)); // 모델명 컬럼 내림차순 정렬
                sorter.setSortKeys(sortKeys);
            }
        });

        // 테이블 업데이트
        loadProductTable();

        // Action Buttons Panel
        JPanel actionPanel = new JPanel();
        actionPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

        JButton updateButton = new JButton("모델 수정");
        JButton deleteButton = new JButton("모델 삭제");
        JButton deleteAllButton = new JButton("전체 모델 삭제");

        actionPanel.add(updateButton);
        actionPanel.add(deleteButton);
        actionPanel.add(deleteAllButton);

        add(actionPanel, BorderLayout.SOUTH);

        // Button Actions
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addProduct();
            }
        });

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchProduct();
            }
        });

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateProduct();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteProduct();
            }
        });

        deleteAllButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteAllProducts();
            }
        });

        // 테이블 더블 클릭 이벤트 추가
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int selectedRow = table.convertRowIndexToModel(table.getSelectedRow());
                    if (selectedRow != -1) {
                        int pid = Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString());
                        String modelName = tableModel.getValueAt(selectedRow, 1).toString();
                        int currentQuantity = Integer.parseInt(tableModel.getValueAt(selectedRow, 3).toString());
                        showInventoryPopup(pid, modelName, currentQuantity);
                    }
                }
            }
        });

        setVisible(true);
    }

    // 입고 및 출고 팝업 창 표시
    private void showInventoryPopup(int pid, String modelName, int currentQuantity) {
        JDialog dialog = new JDialog(this, "재고 관리 - " + modelName, true);
        dialog.setSize(400, 300);
        dialog.setLayout(new GridBagLayout());
        dialog.setLocationRelativeTo(this);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel currentQuantityLabel = new JLabel("현재 수량: " + currentQuantity);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        dialog.add(currentQuantityLabel, gbc);

        JLabel quantityLabel = new JLabel("수량:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        dialog.add(quantityLabel, gbc);

        JTextField quantityField = new JTextField(10);
        gbc.gridx = 1;
        gbc.gridy = 1;
        dialog.add(quantityField, gbc);

        JButton addButton = new JButton("입고하기");
        JButton removeButton = new JButton("출고하기");

        gbc.gridx = 0;
        gbc.gridy = 2;
        dialog.add(addButton, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        dialog.add(removeButton, gbc);

        JTextArea historyArea = new JTextArea(5, 30);
        historyArea.setEditable(false);
        JScrollPane historyScrollPane = new JScrollPane(historyArea);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        dialog.add(historyScrollPane, gbc);

        final int[] currentQuantityWrapper = {currentQuantity};

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int quantityToAdd = Integer.parseInt(quantityField.getText());
                    int newQuantity = currentQuantityWrapper[0] + quantityToAdd;
                    updateProductQuantity(pid, newQuantity);
                    currentQuantityLabel.setText("현재 수량: " + newQuantity);
                    historyArea.append("입고: " + quantityToAdd + " -> 수정 후 재고: " + newQuantity + "\n");
                    currentQuantityWrapper[0] = newQuantity;
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(dialog, "유효한 수량을 입력하세요.");
                }
            }
        });

        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int quantityToRemove = Integer.parseInt(quantityField.getText());
                    if (quantityToRemove > currentQuantityWrapper[0]) {
                        JOptionPane.showMessageDialog(dialog, "출고 수량이 현재 수량보다 많을 수 없습니다.");
                        return;
                    }
                    int newQuantity = currentQuantityWrapper[0] - quantityToRemove;
                    updateProductQuantity(pid, newQuantity);
                    currentQuantityLabel.setText("현재 수량: " + newQuantity);
                    historyArea.append("출고: " + quantityToRemove + " -> 수정 후 재고: " + newQuantity + "\n");
                    currentQuantityWrapper[0] = newQuantity;
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(dialog, "유효한 수량을 입력하세요.");
                }
            }
        });

        dialog.setVisible(true);
    }

    // 재고 수량 업데이트 메서드
    private void updateProductQuantity(int pid, int newQuantity) {
        Session s = null;
        try {
            HibernateCFGcode cfg = new HibernateCFGcode();
            s = cfg.getSession();
            Transaction tx = s.beginTransaction();
            product p = s.get(product.class, pid);
            if (p != null) {
                p.setPquantity(newQuantity);
                s.update(p);
                tx.commit();
                loadProductTable(); // 테이블 업데이트
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "재고 수량 업데이트 중 에러: " + e.getMessage());
        } finally {
            if (s != null) {
                s.close();
            }
        }
    }

    // 모델 추가 메서드
    private void addProduct() {
        Session s = null;
        boolean success = false;  // 추가 성공 여부를 나타내는 플래그 변수

        try {
            HibernateCFGcode cfg = new HibernateCFGcode();
            s = cfg.getSession();
            Transaction tx = s.beginTransaction();

            // 각 필드에 대해 빈 값인지 확인하고 빈 값이면 null로 설정
            String pname = pnameField.getText().isEmpty() ? null : pnameField.getText(); // 모델명이 비어있으면 null로 저장
            Integer price = priceField.getText().isEmpty() ? null : Integer.parseInt(priceField.getText()); // 단가가 비어있으면 null로 저장
            Integer quantity = quantityField.getText().isEmpty() ? null : Integer.parseInt(quantityField.getText()); // 수량이 비어있으면 null로 저장
            String location = locationField.getText().isEmpty() ? null : locationField.getText(); // 장소가 비어있으면 null로 저장
            String detailLocation = detailLocationField.getText().isEmpty() ? null : detailLocationField.getText(); // 세부 장소가 비어있으면 null로 저장

            // 새 제품 객체 생성
            product p = new product(pname, price, quantity, location, detailLocation);
            s.save(p);
            tx.commit();

            success = true;  // 추가 성공 시 플래그를 true로 설정

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "모델 추가 에러: " + e.getMessage());
            loadProductTable();

        } finally {
            if (success) {  // 성공한 경우에만 이 코드를 실행
                JOptionPane.showMessageDialog(this, "모델을 성공적으로 추가했습니다.");
                clearForm();
                loadProductTable(); // 테이블 업데이트
            }

            if (s != null) {
                s.close();
            }
        }
    }

    // 모델 검색 메서드
    private void searchProduct() {
        String searchQuery = pnameField.getText().trim();
        if (searchQuery.isEmpty()) {
            JOptionPane.showMessageDialog(this, "검색할 모델명을 입력하세요.");
            loadProductTable();
            return;
        }

        Session s = null;
        try {
            HibernateCFGcode cfg = new HibernateCFGcode();
            s = cfg.getSession();
            Transaction tx = s.beginTransaction();

            // 모델명으로 검색
            List<product> productList = s.createQuery("from product where pname like :pname", product.class)
                                         .setParameter("pname", "%" + searchQuery + "%")
                                         .list();
            tableModel.setRowCount(0); // 테이블 초기화
            for (product p : productList) {
                Vector<Object> row = new Vector<>();
                row.add(p.getPid());
                row.add(p.getPname());
                row.add(p.getPrice());
                row.add(p.getPquantity());
                row.add(dateFormat.format(p.getUpdatedAt())); // updated_at 필드 추가
                row.add(p.getPlocationField());
                row.add(p.getPdetaillocationField());
                tableModel.addRow(row);
            }
            tx.commit();

            if (productList.isEmpty()) {
                JOptionPane.showMessageDialog(this, "해당 모델을 찾을 수 없습니다.");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "모델 검색 중 에러: " + e.getMessage());
        } finally {
            if (s != null) {
                s.close();
            }
        }
    }

    // 모델 수정 메서드
    private void updateProduct() {
        int selectedRow = table.convertRowIndexToModel(table.getSelectedRow());
        if (selectedRow != -1) {
            int pid = Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString());
            Session s = null;
            try {
                HibernateCFGcode cfg = new HibernateCFGcode();
                s = cfg.getSession();
                Transaction tx = s.beginTransaction();
                product p = s.get(product.class, pid);

                if (p != null) {
                    // Update only non-empty fields
                    if (!pnameField.getText().isEmpty()) {
                        p.setPname(pnameField.getText());
                    }
                    if (!priceField.getText().isEmpty()) {
                        p.setPrice(Integer.parseInt(priceField.getText()));
                    }
                    if (!quantityField.getText().isEmpty()) {
                        p.setPquantity(Integer.parseInt(quantityField.getText()));
                    }
                    if (!locationField.getText().isEmpty()) {
                        p.setPlocationField(locationField.getText());
                    }
                    if (!detailLocationField.getText().isEmpty()) {
                        p.setPdetailLocationField(detailLocationField.getText());
                    }

                    s.update(p);
                    tx.commit();
                    JOptionPane.showMessageDialog(this, "모델을 성공적으로 수정했습니다.");
                    clearForm();
                } else {
                    JOptionPane.showMessageDialog(this, "모델을 찾을 수 없습니다.");
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "모델 수정 중 에러: " + e.getMessage());
            } finally {
                if (s != null) {
                    s.close(); // 세션을 닫음
                }
                loadProductTable(); // 테이블 업데이트
            }
        } else {
            JOptionPane.showMessageDialog(this, "수정할 모델을 선택하세요.");
        }
    }

    // 모델 삭제 메서드
    private void deleteProduct() {
        int selectedRow = table.convertRowIndexToModel(table.getSelectedRow());
        if (selectedRow != -1) {
            int pid = Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString());
            Session s = null;
            try {
                HibernateCFGcode cfg = new HibernateCFGcode();
                s = cfg.getSession();
                Transaction tx = s.beginTransaction();
                product p = s.get(product.class, pid);
                if (p != null) {
                    s.delete(p);
                    tx.commit();
                    JOptionPane.showMessageDialog(this, "모델을 성공적으로 삭제했습니다.");
                    loadProductTable();
                } else {
                    JOptionPane.showMessageDialog(this, "모델을 찾을 수 없습니다.");
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "모델 삭제 중 에러: " + e.getMessage());
            } finally {
                if (s != null) {
                    s.close(); // 세션을 닫음
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "삭제할 모델을 선택하세요.");
        }
    }

    // 전체 모델 삭제 메서드
    private void deleteAllProducts() {
        Session s = null;
        try {
            HibernateCFGcode cfg = new HibernateCFGcode();
            s = cfg.getSession();
            Transaction tx = s.beginTransaction();
            s.createQuery("DELETE FROM product").executeUpdate();  // 모든 모델 삭제 쿼리
            tx.commit();
            tableModel.setRowCount(0); // 테이블에서 모든 행 제거
            JOptionPane.showMessageDialog(this, "전체 모델을 성공적으로 삭제했습니다.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "전체 모델 삭제 에러: " + e.getMessage());
        } finally {
            if (s != null) {
                s.close();
            }
        }
    }

    private void loadProductTable() {
        Session s = null;
        try {
            HibernateCFGcode cfg = new HibernateCFGcode();
            s = cfg.getSession();
            Transaction tx = s.beginTransaction();
            List<product> productList = s.createQuery("from product", product.class).list();
            tableModel.setRowCount(0);
            for (product p : productList) {
                Vector<Object> row = new Vector<>();
                row.add(p.getPid());
                row.add(p.getPname());
                row.add(p.getPrice() != null ? p.getPrice() + " 원" : "");
                row.add(p.getPquantity() != null ? p.getPquantity() : "");
                row.add(dateFormat.format(p.getUpdatedAt()));  // updated_at 필드 추가
                row.add(p.getPlocationField());
                row.add(p.getPdetaillocationField());
                tableModel.addRow(row);
            }
            tx.commit();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "모델 로드 중 에러: " + e.getMessage());
        } finally {
            if (s != null) {
                s.close();
            }
        }
    }

    private void clearForm() {
        pnameField.setText("");
        priceField.setText("");
        quantityField.setText("");
        locationField.setText("");
        detailLocationField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new InventoryApp());
    }
}
