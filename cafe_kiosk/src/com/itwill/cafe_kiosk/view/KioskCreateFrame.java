package com.itwill.cafe_kiosk.view;

import java.awt.Component;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.itwill.cafe_kiosk.controller.KioskDao;
import com.itwill.cafe_kiosk.model.Kiosk;
import com.itwill.cafe_kiosk.view.KioskManage.LoginNotify;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class KioskCreateFrame extends JFrame {
	
	public interface CreateNotify {
		void createSuccess();
	}

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textMenuName;
	private JLabel lblMenuName;
	private JTextField textPrice;
	
	KioskDao dao = KioskDao.getInstance();
	private LoginNotify appMain;
	private CreateNotify appManage;
	private Component parentComponent;
	
	private static final String[] SEARCH_TYPES = {"Ice drink", "Hot drink", "Ade", "Dessert"};
	private JComboBox<String> comboBox;
	private JTextField textPhoto;
	private JLabel lblPhoto;
	private JButton btnAddMenu;
	private JButton btnCancle;
	private JLabel lblType;
	private JLabel lblPrice;

	/**
	 * Launch the application.
	 */
	public static void showKioskCreateFrame(LoginNotify appMain, CreateNotify appManage, Component parentComponent) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					KioskCreateFrame frame = new KioskCreateFrame(appMain, appManage, parentComponent);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public KioskCreateFrame(LoginNotify appMain, CreateNotify appManage, Component parentComponent) {
		this.appMain = appMain;
		this.appManage = appManage;
		this.parentComponent = parentComponent;
		initialize();
		
	}

	/**
	 * Create the frame.
	 */
	public void initialize() {
		setTitle("메뉴 추가");
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
		
		btnAddMenu = new JButton("메뉴 추가");
		btnAddMenu.addActionListener((e) -> createMenu());
		btnAddMenu.setFont(new Font("맑은 고딕", Font.BOLD, 25));
		btnAddMenu.setBounds(37, 370, 150, 47);
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

	private void createMenu() {
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
		int result = dao.create(kiosk);
		
		if (result == 1) {
			appManage.createSuccess();
			dispose();
		} else {
			JOptionPane.showMessageDialog(contentPane, "저장에 실패했습니다.");
		}
		
		
	}
}
