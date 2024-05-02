package com.itwill.cafe_kiosk.view;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;

import com.itwill.cafe_kiosk.controller.ManagerDao;
import com.itwill.cafe_kiosk.model.Manager;
import com.itwill.cafe_kiosk.view.KioskManage.LoginNotify;
import com.itwill.cafe_kiosk.view.KioskMenu.MenuNotify;
import com.itwill.cafe_kiosk.view.ManagerCreateFrame.ManagerCreateNotify;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class KioskMain implements LoginNotify, MenuNotify, ManagerCreateNotify {

	private JFrame frame;
	private JTextField textId;
	private JTextField textPassword;
	private JButton btnLogin;
	private JButton btnSimpleLogin;
	private JLabel lblManagerId;
	private JLabel lblManagerPassword;
	private JButton btnBackToMainmenu;
	private JButton btnCreateManager;
	
	private ManagerDao dao = ManagerDao.getInstance();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					KioskMain window = new KioskMain();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public KioskMain() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 640, 560);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		btnBackToMainmenu = new JButton("구매 화면");
		btnBackToMainmenu.setBounds(12, 413, 600, 98);
		btnBackToMainmenu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				KioskMenu.showKioskMenu(frame, KioskMain.this);
			}
		});
		btnBackToMainmenu.setFont(new Font("맑은 고딕", Font.PLAIN, 30));
		
		lblManagerId = new JLabel("관리자 아이디");
		lblManagerId.setBounds(93, 184, 111, 31);
		lblManagerId.setFont(new Font("맑은 고딕", Font.PLAIN, 15));
		
		lblManagerPassword = new JLabel("관리자 비밀번호");
		lblManagerPassword.setBounds(93, 231, 111, 31);
		lblManagerPassword.setFont(new Font("맑은 고딕", Font.PLAIN, 15));
		
		textId = new JTextField();
		textId.setBounds(235, 186, 172, 33);
		textId.setColumns(10);
		
		textPassword = new JTextField();
		textPassword.setBounds(235, 229, 172, 33);
		textPassword.setColumns(10);
		
		btnLogin = new JButton("관리자 로그인");
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Manager manager = dao.read(textId.getText());
				if (manager == null) { 
					JOptionPane.showMessageDialog(frame, "일치하는 아이디가 없습니다.", "로그인 실패", JOptionPane.INFORMATION_MESSAGE);
					return;
				}
				if (!manager.getPassword().equals(textPassword.getText())) {
					JOptionPane.showMessageDialog(frame, "비밀번호가 일치하지 않습니다.", "로그인 실패", JOptionPane.INFORMATION_MESSAGE);
					return;
				}
				textId.setText("");
				textPassword.setText("");
				KioskManage.showManageMenu(frame, KioskMain.this); // 관리자화면 이동.
			}
		});
		btnLogin.setBounds(425, 184, 172, 78);
		btnLogin.setFont(new Font("맑은 고딕", Font.PLAIN, 20));
		
		btnSimpleLogin = new JButton("임시로그인");
		btnSimpleLogin.setBounds(12, 10, 172, 84);
		btnSimpleLogin.addActionListener(e -> {
			KioskManage.showManageMenu(frame, KioskMain.this);
			});
		btnSimpleLogin.setFont(new Font("맑은 고딕", Font.PLAIN, 20));
		frame.getContentPane().setLayout(null);
		frame.getContentPane().add(lblManagerPassword);
		frame.getContentPane().add(lblManagerId);
		frame.getContentPane().add(btnSimpleLogin);
		frame.getContentPane().add(textPassword);
		frame.getContentPane().add(textId);
		frame.getContentPane().add(btnLogin);
		frame.getContentPane().add(btnBackToMainmenu);
		
		btnCreateManager = new JButton("관리자등록");
		btnCreateManager.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ManagerCreateFrame.showManagerCreate(frame, KioskMain.this);
			}
		});
		btnCreateManager.setFont(new Font("맑은 고딕", Font.PLAIN, 13));
		btnCreateManager.setBounds(257, 272, 111, 31);
		frame.getContentPane().add(btnCreateManager);
	}

	@Override
	public void LoginSuccess() {
		frame.setVisible(false); // 창 띄우고 원래 창 닫기.	
	}

	@Override
	public void menuSuccess() {
		frame.setVisible(false); // 창 띄우고 원래 창 닫기.	
	}

	@Override
	public void managerCreateSuccess() {
		JOptionPane.showMessageDialog(frame, "회원가입이 완료되었습니다.");
	}
}
