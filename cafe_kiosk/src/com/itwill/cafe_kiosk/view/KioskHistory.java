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
import javax.swing.JTextField;

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
	private JLabel lblTotal;
	private JTextField textTotal;
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
		contentPane.setLayout(null);
		
		lblHistory = new JLabel("판매 내역");
		lblHistory.setBounds(5, 5, 614, 41);
		lblHistory.setHorizontalAlignment(SwingConstants.CENTER);
		lblHistory.setFont(new Font("맑은 고딕", Font.BOLD, 30));
		contentPane.add(lblHistory);
		
		panelBtn = new JPanel();
		panelBtn.setBounds(5, 429, 614, 107);
		contentPane.add(panelBtn);
		
		btnDeleteTable = new JButton("테이블 삭제");
		btnDeleteTable.setBounds(125, 54, 180, 43);
		btnDeleteTable.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				deleteHistoryTable();
			}


		});
		panelBtn.setLayout(null);
		btnDeleteTable.setFont(new Font("맑은 고딕", Font.BOLD, 25));
		panelBtn.add(btnDeleteTable);
		
		JButton btnCancel = new JButton("닫기");
		btnCancel.setBounds(353, 54, 96, 43);
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnCancel.setFont(new Font("맑은 고딕", Font.BOLD, 25));
		panelBtn.add(btnCancel);
		
		lblTotal = new JLabel("매출 합계:");
		lblTotal.setFont(new Font("맑은 고딕", Font.BOLD, 20));
		lblTotal.setBounds(196, 10, 96, 34);
		panelBtn.add(lblTotal);
		
		textTotal = new JTextField();
		textTotal.setFont(new Font("맑은 고딕", Font.BOLD, 20));
		textTotal.setHorizontalAlignment(SwingConstants.RIGHT);
		textTotal.setBounds(292, 10, 157, 34);
		panelBtn.add(textTotal);
		textTotal.setColumns(10);
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(5, 46, 614, 383);
		contentPane.add(scrollPane);
		
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
		
		setTotal(dao.readHistory()); // 매출 합계를 초기화하기 위한 메서드. 테이블이 업데이트 될 때 마다 매출합계도 업데이트
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
	
	private void setTotal(List<Kiosk> kiosks) {
		int sum = 0 ;
		for (Kiosk k : kiosks) {
			sum += k.getPrice() * k.getCount();
		}
		textTotal.setText(sum + " 원");
	}
}
