package com.itwill.cafe_kiosk.view;

import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import com.itwill.cafe_kiosk.controller.KioskDao;
import com.itwill.cafe_kiosk.model.Kiosk;
import com.itwill.cafe_kiosk.view.KioskManage.LoginNotify;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class KioskDetailFrame extends JFrame {
	
	public interface UpdateNotify {
		void updateSuccess();
	}

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textMenuName;
	private JLabel lblMenuName;
	private JTextField textPrice;
	private JComboBox<String> comboBox;
	private JTextField textPhoto;
	private JLabel lblPhoto;
	private JButton btnAddMenu;
	private JButton btnCancle;
	private JLabel lblType;
	private JLabel lblPrice;
	private static final String[] SEARCH_TYPES = {"Ice drink", "Hot drink", "Ade", "Dessert"};

	KioskDao dao = KioskDao.getInstance();
	Component parentComponent;
	String menuName;
	UpdateNotify appManage;
	LoginNotify appMain;
	int index;

	/**
	 * Launch the application.
	 */
	public static void showKioskDetailsFrame(Component parentComponent, 
			String menuName, 
			UpdateNotify appManage, 
			LoginNotify appMain,
			int index) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					KioskDetailFrame frame = new KioskDetailFrame(parentComponent,
							menuName,
							appManage,
							appMain,
							index);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public KioskDetailFrame(Component parentComponent, 
							String menuName,
							UpdateNotify appManage,
							LoginNotify appMain,
							int index) {
		this.parentComponent = parentComponent;
		this.menuName = menuName;
		this.appManage = appManage;
		this.appMain = appMain;
		initialize();
		initializeTextFields();
	}
	
	public void initializeTextFields() {
		// 3개의 텍스트필드에 해당 인덱스의 연락처 정보를 채움.
		// 자동 생성된 메서드들 사이에 껴있으면 구현이 되어있는지 안되어있는지
		// 구분이 힘드므로 initialize()메서드가 끝난 후 텍스트 필드에 정보를 채운다.
		// initialize()메서드 끝나고 써야하는 이유는 텍스트필드들이 모두 new~~ 하고 초기화가 끝나야
		// setText를 쓸수 있기 때문.
		
		Kiosk kiosk = dao.read(menuName);
		textMenuName.setText(kiosk.getMenuName());
		textPrice.setText(kiosk.getPrice()+"");
		textPhoto.setText(kiosk.getPhoto().replace("imgs/", "").replace(".png", ""));
		comboBox.setSelectedIndex(0);
		if (kiosk.getType().toLowerCase().contains("ice")) {
			comboBox.setSelectedIndex(0);
		} else if (kiosk.getType().toLowerCase().contains("hot")) {
			comboBox.setSelectedIndex(1);
		} else if (kiosk.getType().toLowerCase().contains("ade")) {
			comboBox.setSelectedIndex(2);
		} else if (kiosk.getType().toLowerCase().contains("dessert")) {
			comboBox.setSelectedIndex(3);
		}
		
	}
	
	/**
	 * Create the frame.
	 */
	public void initialize() {
		setTitle("메뉴 수정");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		int x = 0;
		int y = 0;
		if (parentComponent != null) {
			x = parentComponent.getX(); // 부모 컴포넌트의 x좌표
			y = parentComponent.getY(); // 부모 컴포넌트의 y좌표
		}
		setBounds(x, y, 480	, 480);
		
		if (parentComponent == null ) {
			setLocationRelativeTo(null); // 화면 중앙에 JFrame을 띄움
		}
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		lblMenuName = new JLabel("메뉴 이름");
		lblMenuName.setHorizontalAlignment(SwingConstants.CENTER);
		lblMenuName.setFont(new Font("맑은 고딕", Font.BOLD, 20));
		lblMenuName.setBounds(12, 43, 120, 47);
		contentPane.add(lblMenuName);
		
		textMenuName = new JTextField();
		textMenuName.setFont(new Font("맑은 고딕", Font.PLAIN, 20));
		textMenuName.setHorizontalAlignment(SwingConstants.LEFT);
		textMenuName.setBounds(144, 43, 308, 47);
		contentPane.add(textMenuName);
		textMenuName.setColumns(10);
		
		lblPrice = new JLabel("가격");
		lblPrice.setHorizontalAlignment(SwingConstants.CENTER);
		lblPrice.setFont(new Font("맑은 고딕", Font.BOLD, 20));
		lblPrice.setBounds(12, 207, 120, 47);
		contentPane.add(lblPrice);
		
		textPrice = new JTextField();
		textPrice.setHorizontalAlignment(SwingConstants.LEFT);
		textPrice.setFont(new Font("맑은 고딕", Font.PLAIN, 20));
		textPrice.setColumns(10);
		textPrice.setBounds(144, 207, 308, 47);
		contentPane.add(textPrice);
		
		lblType = new JLabel("메뉴 종류");
		lblType.setHorizontalAlignment(SwingConstants.CENTER);
		lblType.setFont(new Font("맑은 고딕", Font.BOLD, 20));
		lblType.setBounds(12, 124, 120, 47);
		contentPane.add(lblType);
		
		comboBox = new JComboBox<>();
		comboBox.setFont(new Font("맑은 고딕", Font.BOLD, 20));
		final DefaultComboBoxModel<String> comboBoxModel = new DefaultComboBoxModel<String>(SEARCH_TYPES);
		comboBox.setModel(comboBoxModel);
		comboBox.setSelectedIndex(0);
		comboBox.setBounds(144, 124, 308, 47);
		contentPane.add(comboBox);
		
		textPhoto = new JTextField();
		textPhoto.setHorizontalAlignment(SwingConstants.LEFT);
		textPhoto.setFont(new Font("맑은 고딕", Font.PLAIN, 20));
		textPhoto.setColumns(10);
		textPhoto.setBounds(144, 284, 308, 47);
		contentPane.add(textPhoto);
		
		lblPhoto = new JLabel("사진 이름");
		lblPhoto.setHorizontalAlignment(SwingConstants.CENTER);
		lblPhoto.setFont(new Font("맑은 고딕", Font.BOLD, 20));
		lblPhoto.setBounds(12, 284, 120, 47);
		contentPane.add(lblPhoto);
		
		btnAddMenu = new JButton("수정 완료");
		btnAddMenu.addActionListener((e) -> updateMenu());
		btnAddMenu.setFont(new Font("맑은 고딕", Font.BOLD, 25));
		btnAddMenu.setBounds(37, 370, 151, 47);
		contentPane.add(btnAddMenu);
		
		btnCancle = new JButton("취소");
		btnCancle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnCancle.setFont(new Font("맑은 고딕", Font.BOLD, 25));
		btnCancle.setBounds(281, 370, 142, 47);
		contentPane.add(btnCancle);
	}

	private void updateMenu() {
		String photo = textPhoto.getText();
		String menuName = textMenuName.getText();
		int typeNumber = comboBox.getSelectedIndex();
		String type = null;
		switch (typeNumber) {
		case 0:
			type = "ice";
			break;
		case 1:
			type = "hot";
			break;
		case 2:
			type = "ade";
			break;
		case 3:
			type = "dessert";
			break;
		}
		int price = -1;
		try {
			price = Integer.parseInt(textPrice.getText());
		} catch (Exception e) {
			JOptionPane.showMessageDialog(contentPane, "가격은 숫자로 입력해주세요.", "경고", JOptionPane.WARNING_MESSAGE);
		}
		if (menuName.equals("")) {
			JOptionPane.showMessageDialog(contentPane, "메뉴 이름은 반드시 입력해주세요.", "경고", JOptionPane.WARNING_MESSAGE);
			return;
		}
		Kiosk kiosk = new Kiosk(menuName, price, type, photo);
		int result = dao.update(kiosk);
		
		if (result == 1) {
			appManage.updateSuccess();
			dispose();
		} else {
			JOptionPane.showMessageDialog(contentPane, "저장에 실패했습니다.");
		}
	}
}
