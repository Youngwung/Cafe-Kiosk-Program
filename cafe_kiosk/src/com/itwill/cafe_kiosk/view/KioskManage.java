package com.itwill.cafe_kiosk.view;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import com.itwill.cafe_kiosk.controller.KioskDao;
import com.itwill.cafe_kiosk.model.Kiosk;
import com.itwill.cafe_kiosk.view.KioskCreateFrame.CreateNotify;
import com.itwill.cafe_kiosk.view.KioskDetailFrame.UpdateNotify;
import com.itwill.cafe_kiosk.view.KioskHistory.DeleteHistoryNotify;

import java.awt.Component;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.image.BufferedImage;

import javax.swing.JScrollPane;
import javax.swing.JTable;

import java.util.ArrayList;
import java.util.List;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;


public class KioskManage extends JFrame implements CreateNotify, UpdateNotify, DeleteHistoryNotify {
	
	public interface LoginNotify {
		void LoginSuccess();
	}
	
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JPanel panelType;
	private JButton btnIce;
	private JButton btnHot;
	private JButton btnDessert;
	private JTable table;
	
	private LoginNotify appMain;
	private Component parentComponent;
	BufferedImage image;
	private static final String[] COLUMN_NAMES = {"판매량","메뉴사진", "메뉴이름", "종류", "가격"};
	private DefaultTableModel tableModel;
	KioskDao dao = KioskDao.getInstance();
	DefaultTableCellRenderer celAlignCenter = new DefaultTableCellRenderer(); // 테이블의 셀정렬을 위한 객체 선언.
	
	private JButton btnDeleteMenu;
	private JButton btnUpdateMenu;
	private JButton btnCreateMenu;
	private JPanel panelBtn;
	private JScrollPane scrollPane;
	private JButton btnAde;
	private JButton btnAll;
	private JButton btnTurnBack;
	private JButton btnHistory;
	/**
	 * Launch the application.
	 */
	public static void showManageMenu(Component parentComponent, LoginNotify appMain) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					KioskManage frame = new KioskManage(parentComponent, appMain);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public KioskManage(Component parentComponent, LoginNotify app) {
		this.parentComponent = parentComponent;
		this.appMain = app;
		initialize();
		initializeTable();
		appMain.LoginSuccess();
	}
	
	/**
	 * Create the frame.
	 */
	public void initialize() {
		setTitle("관리자 화면");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		int x = 0;
		int y = 0;
		if (parentComponent != null) {
			x = parentComponent.getX(); // 부모 컴포넌트의 x좌표
			y = parentComponent.getY(); // 부모 컴포넌트의 y좌표
		}
		setBounds(x, y, 700, 611);
		
		if (parentComponent == null ) {
			setLocationRelativeTo(null); // 화면 중앙에 JFrame을 띄움
		}
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		
		panelType = new JPanel();
		
		btnAll = new JButton("전체보기");
		btnAll.addActionListener((e) -> initializeTable());
		btnAll.setFont(new Font("맑은 고딕", Font.PLAIN, 25));
		panelType.add(btnAll);
		
		btnIce = new JButton("Ice");
		btnIce.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				searchIce();
			}


		});
		btnIce.setFont(new Font("맑은 고딕", Font.PLAIN, 25));
		panelType.add(btnIce);
		
		btnHot = new JButton("Hot");
		btnHot.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				searchHot();
			}
		});
		btnHot.setFont(new Font("맑은 고딕", Font.PLAIN, 25));
		panelType.add(btnHot);
		
		btnAde = new JButton("Ade");
		btnAde.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				searchAde();
			}
		});
		btnAde.setFont(new Font("맑은 고딕", Font.PLAIN, 25));
		panelType.add(btnAde);
		
		btnDessert = new JButton("Dessert");
		btnDessert.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				searchDessert();
			}
		});
		btnDessert.setFont(new Font("맑은 고딕", Font.PLAIN, 25));
		panelType.add(btnDessert);
		
		panelBtn = new JPanel();
		
		btnCreateMenu = new JButton("메뉴 추가");
		btnCreateMenu.addActionListener((e) -> KioskCreateFrame.showKioskCreateFrame(appMain, KioskManage.this, parentComponent));
		btnCreateMenu.setFont(new Font("맑은 고딕", Font.BOLD, 20));
		panelBtn.add(btnCreateMenu);
		
		btnUpdateMenu = new JButton("메뉴 수정");
		btnUpdateMenu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				  int index = table.getSelectedRow(); // 테이블에서 선택된 행의 인덱스
			        if (index == -1) { // JTable에서 선택된 행이 없을 때
			            JOptionPane.showMessageDialog(
			                    contentPane, 
			                    "수정할 행을 먼저 선택하세요.", 
			                    "경고", 
			                    JOptionPane.WARNING_MESSAGE);
			            return;
			        }
			        String menuName = (String) tableModel.getValueAt(index, 2);
			        KioskDetailFrame.showKioskDetailsFrame(parentComponent, menuName, KioskManage.this, appMain, index);
			        
			}
		});
		btnUpdateMenu.setFont(new Font("맑은 고딕", Font.BOLD, 20));
		panelBtn.add(btnUpdateMenu);
		
		btnDeleteMenu = new JButton("메뉴 삭제");
		btnDeleteMenu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				deleteMenu();
			}
		});
		btnDeleteMenu.setFont(new Font("맑은 고딕", Font.BOLD, 20));
		panelBtn.add(btnDeleteMenu);
		
		btnTurnBack = new JButton("로그아웃");
		btnTurnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				parentComponent.setVisible(true);
				dispose();
			}
		});
		
		btnHistory = new JButton("판매 내역");
		btnHistory.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				KioskHistory.showKioskHistory(contentPane, KioskManage.this);
			}
		});
		btnHistory.setFont(new Font("맑은 고딕", Font.BOLD, 20));
		panelBtn.add(btnHistory);
		btnTurnBack.setFont(new Font("맑은 고딕", Font.BOLD, 20));
		panelBtn.add(btnTurnBack);
		
		scrollPane = new JScrollPane();
		
		table = new JTable();
		table.setFont(new Font("굴림", Font.PLAIN, 20));
		table.setRowHeight(70);
		scrollPane.setViewportView(table);
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addComponent(panelType, GroupLayout.DEFAULT_SIZE, 619, Short.MAX_VALUE)
				.addComponent(panelBtn, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
				.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 619, Short.MAX_VALUE)
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(Alignment.TRAILING, gl_contentPane.createSequentialGroup()
							.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 418, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)))
					.addComponent(panelType, GroupLayout.PREFERRED_SIZE, 63, GroupLayout.PREFERRED_SIZE)
					.addGap(10)
					.addComponent(panelBtn, GroupLayout.DEFAULT_SIZE, 55, Short.MAX_VALUE))
		);
		contentPane.setLayout(gl_contentPane);

	}
	
	private void deleteMenu() {
		int index = table.getSelectedRow(); // 테이블에서 선택된 행의 인덱스
		if (index == -1) { // JTable에서 선택된 행이 없을 때
			JOptionPane.showMessageDialog( // 경고 메세지 출력.
					contentPane,
					"삭제할 행을 먼저 선택하세요.", 
					"경고", 
					JOptionPane.WARNING_MESSAGE);
			
			return; // 메서드 종료
		}
		int confirm = JOptionPane.showConfirmDialog(
				contentPane,
				"정말 삭제하시겠습니까?", 
				"삭제",
				JOptionPane.YES_NO_OPTION);
		if (confirm == JOptionPane.YES_OPTION) {
			// 선택된 행에서 블로그 번호(id)를 읽음.
			String menuName = (String) tableModel.getValueAt(index, 2); // 까먹기 쉬울듯
			// id로 Dao의 delete() 메서드 호출
			int result = dao.delete(menuName);
			if (result == 1) {
				initializeTable();
				JOptionPane.showMessageDialog(contentPane, "삭제 성공!");
			} else {
				JOptionPane.showMessageDialog(contentPane, "삭제 실패");
				
			}
		}
		
	}
	
	private void resetTable(List<Kiosk> kiosks) {
		tableModel = new DefaultTableModel(null, COLUMN_NAMES) {
			private static final long serialVersionUID = 1L;
			@Override
			public Class<?> getColumnClass(int columnIndex) {
				switch (columnIndex) {
				case 0:
					return Object.class;
				case 1:
					return ImageIcon.class;
				case 2:
				case 3:
					return String.class;
				case 4:
					return Integer.class;
				default:
					return Object.class;
				}
			}
		};
		for (Kiosk k : kiosks) {
			// DB 테이블에서 검색한 레코드를 JTable에서 사용할 행(row) 데이터로 변환.
			Object max = null;
			if (dao.getCount(k.getMenuName()) == setMax(kiosks)) {
				if (dao.getCount(k.getMenuName()) != 0) {
					max = "Best!";
				} else max = dao.getCount(k.getMenuName());
			} else {
				max = dao.getCount(k.getMenuName());
			}
			Object[] row = {
					max,
					new ImageIcon(k.getPhoto()),
					k.getMenuName(),
					k.getType(), k.getPrice() 
			};
			tableModel.addRow(row); // 테이블 모델에 행 데이터를 추가.
		}
		celAlignCenter.setHorizontalAlignment(JLabel.CENTER); // 셀의 정렬을 지정.
		table.setModel(tableModel); // JTable의 모델을 다시 세팅(보여주기)
		table.getColumn(COLUMN_NAMES[0]).setPreferredWidth(30); // 0번 컬럼의 셀의 너비는 50
		table.getColumn(COLUMN_NAMES[0]).setCellRenderer(celAlignCenter); // 1번컬럼의 셀은 중앙정렬
		table.getColumn(COLUMN_NAMES[1]).setPreferredWidth(50); // 0번 컬럼의 셀의 너비는 50
		table.getColumn(COLUMN_NAMES[2]).setPreferredWidth(150); // 1번 컬럼의 셀의 너비는 150
		table.getColumn(COLUMN_NAMES[2]).setCellRenderer(celAlignCenter); // 1번컬럼의 셀은 중앙정렬
		table.getColumn(COLUMN_NAMES[3]).setPreferredWidth(50);
		table.getColumn(COLUMN_NAMES[3]).setCellRenderer(celAlignCenter);
		table.getColumn(COLUMN_NAMES[4]).setPreferredWidth(50);
		table.getColumn(COLUMN_NAMES[4]).setCellRenderer(celAlignCenter);
	}
	
	private void searchIce() {
		String keyword = "ice";
		List<Kiosk> kiosks = dao.search(keyword);
		resetTable(kiosks);
	}

	private void searchHot() {
		String keyword = "hot";
		List<Kiosk> kiosks = dao.search(keyword);
		resetTable(kiosks);
	}

	private void searchAde() {
		String keyword = "ade";
		List<Kiosk> kiosks = dao.search(keyword);
		resetTable(kiosks);
	}
	
	private void searchDessert() {
		String keyword = "dessert";
		List<Kiosk> kiosks = dao.search(keyword);
		resetTable(kiosks);
	}
	
	private void initializeTable(){
		List<Kiosk> kiosks = dao.read();
		resetTable(kiosks);
	}

	

	@Override
	public void createSuccess() {
		initializeTable();
		JOptionPane.showMessageDialog(contentPane, "메뉴추가에 성공했습니다.");
	}

	@Override
	public void updateSuccess() {
		initializeTable();		
		JOptionPane.showMessageDialog(contentPane, "메뉴수정에 성공했습니다.");

	}
	
	private int setMax(List<Kiosk> kiosks) {
		int max = Integer.MIN_VALUE;
		List<Integer> numbers= new ArrayList<>();
		for (Kiosk k : kiosks) {
			numbers.add(dao.getCount(k.getMenuName()));
		}
		
		for (int i : numbers) {
		    max = Math.max(max, i);
		}

		return max;
	}

	@Override
	public void deleteHistorySuccess() {
		initializeTable();
		JOptionPane.showMessageDialog(contentPane, "판매내역 삭제에 성공했습니다.");
	}

	
	

}
