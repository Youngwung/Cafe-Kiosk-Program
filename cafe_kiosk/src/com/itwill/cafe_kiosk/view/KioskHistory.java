package com.itwill.cafe_kiosk.view;

import java.awt.Component;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import com.itwill.cafe_kiosk.controller.KioskDao;
import com.itwill.cafe_kiosk.model.Kiosk;

import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.util.List;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class KioskHistory extends JFrame {
	DefaultTableCellRenderer celAlignCenter = new DefaultTableCellRenderer(); // 테이블의 셀정렬을 위한 객체 선언.
	
	public interface DeleteHistoryNotify {
		void deleteHistorySuccess();
	}

	private DeleteHistoryNotify app;
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private Component parentComponent;
	private JButton btnDeleteTable;
	private JPanel panelBtn;
	private JLabel lblHistory;
	private JScrollPane scrollPane;
	private JTable table;
	private DefaultTableModel tableModel;
	private KioskDao dao = KioskDao.getInstance();

	private static final String[] COLUMN_NAMES = {"메뉴 이름", "판매수량", "가격", "매출"};
	/**
	 * Launch the application.
	 */
	public static void showKioskHistory(Component parentComponent, DeleteHistoryNotify app) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					KioskHistory frame = new KioskHistory(parentComponent, app);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public KioskHistory(Component parentComponent, DeleteHistoryNotify app) {
		this.parentComponent = parentComponent;
		this.app = app;
		initialize();
		initializeTable(dao.readHistory());
	}

	/**
	 * Create the frame.
	 */
	public void initialize() {
		setTitle("판매 내역 관리");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		int x = 0;
		int y = 0;
		if (parentComponent != null) {
			x = parentComponent.getX(); // 부모 컴포넌트의 x좌표
			y = parentComponent.getY(); // 부모 컴포넌트의 y좌표
		}
		setBounds(x, y, 640, 580);
		
		if (parentComponent == null ) {
			setLocationRelativeTo(null); // 화면 중앙에 JFrame을 띄움
		}
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		lblHistory = new JLabel("판매 내역");
		lblHistory.setHorizontalAlignment(SwingConstants.CENTER);
		lblHistory.setFont(new Font("맑은 고딕", Font.BOLD, 30));
		contentPane.add(lblHistory, BorderLayout.NORTH);
		
		panelBtn = new JPanel();
		contentPane.add(panelBtn, BorderLayout.SOUTH);
		
		btnDeleteTable = new JButton("테이블 삭제");
		btnDeleteTable.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				deleteHistoryTable();
			}


		});
		btnDeleteTable.setFont(new Font("맑은 고딕", Font.BOLD, 25));
		panelBtn.add(btnDeleteTable);
		
		JButton btnCancel = new JButton("닫기");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnCancel.setFont(new Font("맑은 고딕", Font.BOLD, 25));
		panelBtn.add(btnCancel);
		
		scrollPane = new JScrollPane();
		contentPane.add(scrollPane, BorderLayout.CENTER);
		
		table = new JTable();
		table.setFont(new Font("맑은 고딕", Font.PLAIN, 20));
		table.setRowHeight(30);
		scrollPane.setViewportView(table);
	}
	
	private void initializeTable(List<Kiosk> kiosks) {
		tableModel = new DefaultTableModel(null, COLUMN_NAMES);
		for (Kiosk k : kiosks) {
			Object[] row = {
					k.getMenuName(),
					k.getCount(), k.getPrice()+ " 원" ,
					(k.getCount() * k.getPrice())+" 원"
			};
			tableModel.addRow(row); // 테이블 모델에 행 데이터를 추가.
		}
		table.setModel(tableModel); // JTable의 모델을 다시 세팅(보여주기)
		celAlignCenter.setHorizontalAlignment(JLabel.CENTER);
		table.getColumn(COLUMN_NAMES[0]).setCellRenderer(celAlignCenter);
		table.getColumn(COLUMN_NAMES[1]).setCellRenderer(celAlignCenter);
		table.getColumn(COLUMN_NAMES[2]).setCellRenderer(celAlignCenter);
		table.getColumn(COLUMN_NAMES[3]).setCellRenderer(celAlignCenter);
	}
	
	private void deleteHistoryTable() {
		int result = JOptionPane.showConfirmDialog(contentPane, "정말로 판매 내역을 삭제하시겠습니까?", "판매 내역 삭제", DISPOSE_ON_CLOSE, JOptionPane.YES_NO_OPTION);
		if (result == JOptionPane.YES_OPTION) {
			dao.deleteHistory();
			initializeTable(dao.readHistory());
			dispose();
			app.deleteHistorySuccess();
		} 
	}

}
