package com.itwill.cafe_kiosk.view;

import java.awt.Component;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.itwill.cafe_kiosk.controller.ManagerDao;
import com.itwill.cafe_kiosk.model.Manager;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.util.List;
import java.awt.event.ActionEvent;

public class ManagerCreateFrame extends JFrame {
	
	public interface ManagerCreateNotify{
		void managerCreateSuccess();
	}

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private Component parentComponent;
	private ManagerCreateNotify app;
	private JLabel lblName;
	private JTextField textName;
	private JTextField textId;
	private JTextField textPassword;
	private JTextField textPassword2;
	private JButton btnCreate;
	
	private ManagerDao dao = ManagerDao.getInstance();

	/**
	 * Launch the application.
	 */
	public static void showManagerCreate(Component parentComponent, ManagerCreateNotify app) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ManagerCreateFrame frame = new ManagerCreateFrame(parentComponent, app);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	public ManagerCreateFrame(Component parentComponent, ManagerCreateNotify app) {
		this.parentComponent = parentComponent;
		this.app = app;
		initialize();
	}

	public void initialize() {
		setTitle("관리자 등록");
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
		
		lblName = new JLabel("이름 입력");
		lblName.setHorizontalAlignment(SwingConstants.CENTER);
		lblName.setFont(new Font("맑은 고딕", Font.BOLD, 20));
		lblName.setBounds(12, 96, 146, 37);
		contentPane.add(lblName);
		
		textName = new JTextField();
		textName.setFont(new Font("굴림", Font.BOLD, 20));
		textName.setBounds(170, 96, 282, 37);
		contentPane.add(textName);
		textName.setColumns(10);
		
		JLabel lblId = new JLabel("아이디 입력");
		lblId.setHorizontalAlignment(SwingConstants.CENTER);
		lblId.setFont(new Font("맑은 고딕", Font.BOLD, 20));
		lblId.setBounds(12, 167, 146, 37);
		contentPane.add(lblId);
		
		textId = new JTextField();
		textId.setFont(new Font("굴림", Font.BOLD, 20));
		textId.setColumns(10);
		textId.setBounds(170, 167, 282, 37);
		contentPane.add(textId);
		
		JLabel lblPassword1 = new JLabel("비밀번호 입력");
		lblPassword1.setHorizontalAlignment(SwingConstants.CENTER);
		lblPassword1.setFont(new Font("맑은 고딕", Font.BOLD, 20));
		lblPassword1.setBounds(12, 243, 146, 37);
		contentPane.add(lblPassword1);
		
		textPassword = new JTextField();
		textPassword.setFont(new Font("굴림", Font.BOLD, 20));
		textPassword.setColumns(10);
		textPassword.setBounds(170, 243, 282, 37);
		contentPane.add(textPassword);
		
		JLabel lblPassword1_1 = new JLabel("비밀번호 입력");
		lblPassword1_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblPassword1_1.setFont(new Font("맑은 고딕", Font.BOLD, 20));
		lblPassword1_1.setBounds(12, 315, 146, 37);
		contentPane.add(lblPassword1_1);
		
		textPassword2 = new JTextField();
		textPassword2.setFont(new Font("굴림", Font.BOLD, 20));
		textPassword2.setColumns(10);
		textPassword2.setBounds(170, 315, 282, 37);
		contentPane.add(textPassword2);
		
		JLabel lblTitle = new JLabel("관리자 등록");
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitle.setFont(new Font("맑은 고딕", Font.BOLD, 35));
		lblTitle.setBounds(12, 10, 440, 60);
		contentPane.add(lblTitle);
		
		btnCreate = new JButton("등록");
		btnCreate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				createManager();
			}


		});
		btnCreate.setFont(new Font("맑은 고딕", Font.PLAIN, 25));
		btnCreate.setBounds(75, 379, 125, 52);
		contentPane.add(btnCreate);
		
		JButton btnCancel = new JButton("취소");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnCancel.setFont(new Font("맑은 고딕", Font.PLAIN, 25));
		btnCancel.setBounds(272, 379, 125, 52);
		contentPane.add(btnCancel);
	}
	
	private void createManager() {

		List<Manager> list = dao.read();
		for (Manager m : list) {
			// 1. 널검사
			boolean isNull = 
					textName.getText().equals("")||
					textId.getText().equals("")||
					textPassword.getText().equals("")||
					textPassword2.getText().equals("");
			if (isNull) {
				JOptionPane.showMessageDialog(
						contentPane, 
						"빈 칸을 모두 채워주세요.",
						"알림", 
						JOptionPane.INFORMATION_MESSAGE);
				return;
			}
			// 2. 아이디 중복검사 
			if (m.getId().equals(textId.getText())) {
				JOptionPane.showMessageDialog(
						contentPane, 
						"중복된 아이디 입니다. 다른 아이디를 입력해 주세요.",
						"아이디 중복", 
						JOptionPane.WARNING_MESSAGE);
				textId.setText("");
				return;
			}
			// 3. 비밀번호 일치 검사 
			if (!textPassword.getText().equals(textPassword2.getText())) {
				JOptionPane.showMessageDialog(
						contentPane, 
						"비밀번호가 일치하지 않습니다.",
						"비밀번호 확인.", 
						JOptionPane.WARNING_MESSAGE);
				textPassword.setText("");
				textPassword2.setText("");
				return;
			}
		}
		// 3.계정 정보 db에 insert
		String name = textName.getText();
		String id = textId.getText();
		String password = textPassword.getText();
		Manager manager = new Manager(name, id, password);
		int result = dao.create(manager);
		if (result != 0) {
			app.managerCreateSuccess();
			dispose();
		} else {
			JOptionPane.showMessageDialog(contentPane, "회원가입에 실패했습니다.");
		}
		
	}
}
