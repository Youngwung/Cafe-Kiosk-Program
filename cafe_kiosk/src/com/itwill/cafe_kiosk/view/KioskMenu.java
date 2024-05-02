package com.itwill.cafe_kiosk.view;

import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import com.itwill.cafe_kiosk.controller.KioskDao;
import com.itwill.cafe_kiosk.model.Kiosk;
import javax.swing.SwingConstants;
import javax.swing.JTextField;

public class KioskMenu extends JFrame {
	
	public interface MenuNotify{
		void menuSuccess();
	}

	private JPanel panelType;
	private JButton btnIce;
	private JButton btnHot;
	private JButton btnDessert;
	private JTable tableMenu;
	
	private Component parentComponent;
	BufferedImage image;
	private static final String[] COLUMN_NAMES = {"판매량", "메뉴사진", "메뉴이름", "종류", "가격"};
	private static final String[] BUY_COLUMN_NAMES = {"메뉴사진", "메뉴이름", "종류", "가격", "개수"};
	private DefaultTableModel tableModel;
	private DefaultTableModel tableBuyModel;
	KioskDao dao = KioskDao.getInstance();
	DefaultTableCellRenderer celAlignCenter = new DefaultTableCellRenderer(); // 테이블의 셀정렬을 위한 객체 선언.
	private JButton btnAddBuyList;
	private JButton btnFastBuy;
	private JPanel panelBtn;
	private JScrollPane scrollMenu;
	private JButton btnAde;
	private JButton btnAll;

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JLabel lblBuy;
	private JScrollPane scrollBuy;
	private JTable tableBuy;
	private JTextField textTotal;
	private JPanel panelTotal;
	private JLabel lblTotal;
	private JPanel panelBtn_1;
	private JButton btnAllBuy;
	private JButton btnAllDelete;
	private JButton btnSelectDelete;
	private JLabel lblMenu;
	private JButton btnManage;
	
	List<Kiosk> buyList = new ArrayList<Kiosk>();
	private Component component;
	private MenuNotify app;

	/**
	 * Launch the application.
	 */
	public static void showKioskMenu(Component component, MenuNotify app) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					KioskMenu frame = new KioskMenu(component, app);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	public KioskMenu() {
		initialize();
	}
	public KioskMenu (Component component, MenuNotify app) {
		this.component = component;
		this.app = app;
		buyList = new ArrayList<Kiosk>();
		initialize();
		initializeTable();
		app.menuSuccess();
	}
	
	
	/**
	 * Create the frame.
	 */
	public void initialize() {
		setTitle("구매 화면");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		int x = 0;
		int y = 0;
		if (parentComponent != null) {
			x = parentComponent.getX(); // 부모 컴포넌트의 x좌표
			y = parentComponent.getY(); // 부모 컴포넌트의 y좌표
		}
		setBounds(x, y, 1280, 680);
		
		if (parentComponent == null ) {
			setLocationRelativeTo(null); // 화면 중앙에 JFrame을 띄움
		}
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		
		panelType = new JPanel();
		panelType.setBounds(5, 501, 630, 53);
		
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
		panelBtn.setBounds(5, 564, 630, 77);
		panelBtn.setLayout(null);
		
		btnFastBuy = new JButton("바로 구매");
		btnFastBuy.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addHistoryFastBuy();
			}


		});
		btnFastBuy.setBounds(62, 10, 207, 63);
		btnFastBuy.setFont(new Font("맑은 고딕", Font.BOLD, 35));
		panelBtn.add(btnFastBuy);
		
		btnAddBuyList = new JButton("장바구니 담기");
		btnAddBuyList.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				List<Kiosk> kiosks = addBuyList();
				resetBuyTable(kiosks);
			}


		});
		btnAddBuyList.setBounds(301, 10, 287, 63);
		btnAddBuyList.setFont(new Font("맑은 고딕", Font.BOLD, 35));
		panelBtn.add(btnAddBuyList);
		
		scrollMenu = new JScrollPane();
		scrollMenu.setBounds(5, 54, 630, 437);
		
		tableMenu = new JTable();
		tableMenu.setFont(new Font("굴림", Font.PLAIN, 20));
		tableMenu.setRowHeight(70);
		scrollMenu.setViewportView(tableMenu);
		
		scrollBuy = new JScrollPane();
		scrollBuy.setBounds(652, 54, 600, 437);
		
		tableBuy = new JTable();
		tableBuy.setFont(new Font("굴림", Font.PLAIN, 20));
		tableBuy.setRowHeight(70);
		scrollBuy.setViewportView(tableBuy);
		
		lblMenu = new JLabel("메뉴");
		lblMenu.setBounds(5, 10, 630, 34);
		lblMenu.setFont(new Font("맑은 고딕", Font.BOLD, 35));
		lblMenu.setHorizontalAlignment(SwingConstants.CENTER);
		
		lblBuy = new JLabel("장바구니");
		lblBuy.setBounds(807, 10, 288, 34);
		lblBuy.setHorizontalAlignment(SwingConstants.CENTER);
		lblBuy.setFont(new Font("맑은 고딕", Font.BOLD, 35));
		contentPane.setLayout(null);
		contentPane.add(lblMenu);
		contentPane.add(panelType);
		contentPane.add(lblBuy);
		contentPane.add(panelBtn);
		contentPane.add(scrollMenu);
		contentPane.add(scrollBuy);
		
		panelBtn_1 = new JPanel();
		panelBtn_1.setBounds(654, 578, 598, 53);
		contentPane.add(panelBtn_1);
		
		btnAllBuy = new JButton("구매 확정");
		btnAllBuy.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				for (Kiosk k : buyList) {
					for (int i = 0; i < k.getCount(); i++ ) {
						dao.addHistory(k.getMenuName(), k.getPrice());
					}
				}
				buyList = new ArrayList<Kiosk>();
				resetTotal();
				resetBuyTable(buyList);
				List<Kiosk> kiosks = dao.read();
				resetTable(kiosks);
			}
		});
		btnAllBuy.setFont(new Font("맑은 고딕", Font.BOLD, 25));
		panelBtn_1.add(btnAllBuy);
		
		btnAllDelete = new JButton("모두 비우기");
		btnAllDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				buyList = new ArrayList<Kiosk>();
				resetTotal();
				resetBuyTable(buyList);
				
			}
		});
		btnAllDelete.setFont(new Font("맑은 고딕", Font.BOLD, 25));
		panelBtn_1.add(btnAllDelete);
		
		btnSelectDelete = new JButton("선택한 메뉴 지우기");
		btnSelectDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				delectByIndex();

			}
		});
		btnSelectDelete.setFont(new Font("맑은 고딕", Font.BOLD, 25));
		panelBtn_1.add(btnSelectDelete);
		
		panelTotal = new JPanel();
		panelTotal.setBounds(657, 501, 590, 53);
		contentPane.add(panelTotal);
		panelTotal.setLayout(null);
		
		textTotal = new JTextField();
		textTotal.setHorizontalAlignment(SwingConstants.RIGHT);
		textTotal.setBounds(171, 0, 427, 53);
		panelTotal.add(textTotal);
		textTotal.setFont(new Font("맑은 고딕", Font.BOLD, 35));
		textTotal.setColumns(10);
		
		lblTotal = new JLabel("결제 금액: ");
		lblTotal.setBounds(0, 3, 205, 49);
		panelTotal.add(lblTotal);
		lblTotal.setFont(new Font("맑은 고딕", Font.BOLD, 35));
		
		btnManage = new JButton("관리자 페이지");
		btnManage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				component.setVisible(true);
				dispose();
			}
		});
		btnManage.setFont(new Font("맑은 고딕", Font.PLAIN, 15));
		btnManage.setBounds(1107, 10, 140, 34);
		contentPane.add(btnManage);
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
	private void resetTable(List<Kiosk> kiosks) {
		tableModel = new DefaultTableModel(null, COLUMN_NAMES) {
			private static final long serialVersionUID = 1L;
			@Override
			public Class<?> getColumnClass(int columnIndex) {
				switch (columnIndex) {
				case 0:
					return Integer.class;
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
		tableMenu.setModel(tableModel); // JTable의 모델을 다시 세팅(보여주기)
		tableMenu.getColumn(COLUMN_NAMES[0]).setPreferredWidth(50); // 0번 컬럼의 셀의 너비는 50
		tableMenu.getColumn(COLUMN_NAMES[0]).setCellRenderer(celAlignCenter);
		tableMenu.getColumn(COLUMN_NAMES[1]).setPreferredWidth(50); // 1번 컬럼의 셀의 너비는 150
		tableMenu.getColumn(COLUMN_NAMES[2]).setPreferredWidth(150);
		tableMenu.getColumn(COLUMN_NAMES[2]).setCellRenderer(celAlignCenter);
		tableMenu.getColumn(COLUMN_NAMES[3]).setPreferredWidth(50);
		tableMenu.getColumn(COLUMN_NAMES[3]).setCellRenderer(celAlignCenter);
		tableMenu.getColumn(COLUMN_NAMES[4]).setPreferredWidth(50);
		tableMenu.getColumn(COLUMN_NAMES[4]).setCellRenderer(celAlignCenter);
		
	}
	
	private void resetBuyTable(List<Kiosk> kiosks) {
		tableBuyModel = new DefaultTableModel(null, BUY_COLUMN_NAMES) {
			private static final long serialVersionUID = 1L;
			
			@Override
			public Class<?> getColumnClass(int columnIndex) {
				switch (columnIndex) {
				case 0:
					return ImageIcon.class;
				case 1:
				case 2:
					return String.class;
				case 3:
					return Integer.class;
				case 4:
					return Object.class;
				default:
					return Object.class;
				}
			}
		};
		for (Kiosk k : kiosks) {
			// DB 테이블에서 검색한 레코드를 JTable에서 사용할 행(row) 데이터로 변환.
			Object[] row = {
					new ImageIcon(k.getPhoto()),
					k.getMenuName(),
					k.getType(), k.getPrice(),
					k.getCount()
			};
			tableBuyModel.addRow(row); // 테이블 모델에 행 데이터를 추가.
		}
		celAlignCenter.setHorizontalAlignment(JLabel.CENTER); // 셀의 정렬을 지정.
		tableBuy.setModel(tableBuyModel); // JTable의 모델을 다시 세팅(보여주기)
		tableBuy.getColumn(BUY_COLUMN_NAMES[0]).setPreferredWidth(50); // 1번 컬럼의 셀의 너비는 150
		tableBuy.getColumn(BUY_COLUMN_NAMES[1]).setPreferredWidth(150);
		tableBuy.getColumn(BUY_COLUMN_NAMES[1]).setCellRenderer(celAlignCenter);
		tableBuy.getColumn(BUY_COLUMN_NAMES[2]).setPreferredWidth(50);
		tableBuy.getColumn(BUY_COLUMN_NAMES[2]).setCellRenderer(celAlignCenter);
		tableBuy.getColumn(BUY_COLUMN_NAMES[3]).setPreferredWidth(50);
		tableBuy.getColumn(BUY_COLUMN_NAMES[3]).setCellRenderer(celAlignCenter);
		tableBuy.getColumn(BUY_COLUMN_NAMES[4]).setPreferredWidth(50); // 0번 컬럼의 셀의 너비는 50
		tableBuy.getColumn(BUY_COLUMN_NAMES[4]).setCellRenderer(celAlignCenter);
	}
	
	private List<Kiosk> addBuyList() {
		int index = tableMenu.getSelectedRow();
		if (index == -1) { // JTable에서 선택된 행이 없을 때
			JOptionPane.showMessageDialog( // 경고 메세지 출력.
					contentPane,
					"장바구니에 넣을 메뉴를  선택하세요.", 
					"경고", 
					JOptionPane.WARNING_MESSAGE);
		}
		String menuName = (String) tableModel.getValueAt(index, 2);
		
		for (Kiosk k : buyList) {
			if (k.getMenuName().equals(menuName)) {
				k.setCount(k.getCount() + 1);
				resetTotal();
				return buyList;
			}
		}
		
		Kiosk kiosk = dao.read(menuName);
		kiosk.setCount(1);
		buyList.add(kiosk);
		resetTotal();
		return buyList;
	}
	
	private void resetTotal() {
		int sum = 0;
		for (Kiosk k : buyList) {
			sum += k.getPrice() * k.getCount();
		}
		textTotal.setText(sum+" 원");
	}
	
	private void delectByIndex() {
		int index = tableBuy.getSelectedRow(); // 테이블에서 선택된 행의 인덱스
		if (index == -1) { // JTable에서 선택된 행이 없을 때
			JOptionPane.showMessageDialog( // 경고 메세지 출력.
					contentPane,
					"삭제할 행을 먼저 선택하세요.", 
					"경고", 
					JOptionPane.WARNING_MESSAGE);
			return;
		}
		buyList.remove(index);
		resetTotal();
		resetBuyTable(buyList);
	}
	
	private void addHistoryFastBuy() {
		int index = tableMenu.getSelectedRow(); // 테이블에서 선택된 행의 인덱스
		if (index == -1) { // JTable에서 선택된 행이 없을 때
			JOptionPane.showMessageDialog( // 경고 메세지 출력.
					contentPane,
					"구매할 메뉴를 선택하세요.", 
					"경고", 
					JOptionPane.WARNING_MESSAGE);
			return;
		}
		String menuName = (String) tableModel.getValueAt(index, 2);
		int price = (Integer) tableModel.getValueAt(index, 4);
		dao.addHistory(menuName, price);
		List<Kiosk> kiosks = dao.read();
		resetTable(kiosks);
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
	
	
}
