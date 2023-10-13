package GUI;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import java.sql.Timestamp;
import java.sql.Date;
import javax.swing.table.DefaultTableModel;

import Services.AdministratorService;
import Services.CheckLogin;
import Services.NastavnikService;
import Services.ProdekanService;
import Services.StudentService;
import Services.TestService;
import models.AkademskaGodina;
import models.AkademskiPredmet;
import models.Nastavnik;
import models.Predmet;
import models.PredmetOcjena;
import models.Semestar;
import models.Student;
import models.StudijskiProgram;
import models.Usmjerenje;
import models.Zahtjev;
import models.ZahtjevBodovi;
import models.ZahtjevPreduslov;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import org.jdesktop.swingx.prompt.PromptSupport;
import org.jdesktop.swingx.prompt.PromptSupport.FocusBehavior;

public class Login2 extends JFrame {
	private JComboBox<String> ulogeComboBox;
	private JTextField usernameField;
	private JPasswordField passwordField;
	private JButton loginButton;
	private EntityManagerFactory emf = Persistence.createEntityManagerFactory("sistem_za_upis_akademske_godine");
	

	private Student showOcjenaPopup(AkademskiPredmet p, Student s) {
		JTextField input = new JTextField();
		input.setColumns(10);

		NastavnikService nastServ = new NastavnikService(usernameField.getText());

		Object[] message = { "Unesite ocjenu izmedu 5 i 10:", input };

		int option = JOptionPane.showConfirmDialog(null, message, "Unesite ocjenu", JOptionPane.OK_CANCEL_OPTION);

		if (option == JOptionPane.OK_OPTION) {
			try {
				int ocjena = Integer.parseInt(input.getText());
				if (ocjena >= 5 && ocjena <= 10) {
					// Handle the valid input
//                    System.out.println("Entered ocjena: " + ocjena);
					System.out.println(ocjena);
					Student s2=nastServ.dodajOcjenu(p, s, ocjena);

					JOptionPane.showMessageDialog(null, "Ocjena unesena", "Unos", JOptionPane.INFORMATION_MESSAGE);
				    return s2;
				} else {
					// Handle invalid input
//                    System.out.println("Invalid ocjena: " + ocjena);
					JOptionPane.showMessageDialog(null, "Pogresan unos ocjene!", "Greška", JOptionPane.ERROR_MESSAGE);
				    return s;
				}
			} catch (NumberFormatException ex) {
				// Handle invalid input
//                System.out.println("Invalid input format");
				JOptionPane.showMessageDialog(null, "Pogresan unos ocjene!", "Greška", JOptionPane.ERROR_MESSAGE);
				//return s;
			}
			
		}
		return s;
	}

	public Login2() {
		setTitle("Login");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(400, 200);
		setLayout(new GridLayout(5, 2));

		JLabel ulogeLabel = new JLabel("Odaberite ulogu:");
		String[] ulogeOptions = { "Student", "Nastavnik", "Prodekan", "Administrator" };
		ulogeComboBox = new JComboBox<>(ulogeOptions);

		JLabel usernameLabel = new JLabel("Korisničko ime:");
		usernameField = new JTextField();

		JLabel passwordLabel = new JLabel("Lozinka:");
		passwordField = new JPasswordField();

		JLabel emptyLabel1 = new JLabel(""); // Prazan prostor
		JLabel emptyLabel2 = new JLabel(""); // Prazan prostor

		loginButton = new JButton("Prijava");
		loginButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String selectedUloga = (String) ulogeComboBox.getSelectedItem();
				String username = usernameField.getText();
				String password = new String(passwordField.getPassword());
				CheckLogin checkLogin = new CheckLogin();
				if (checkLogin.authenticate(username, password, selectedUloga)) {
					Student s = null;
					if (selectedUloga.equals("Student")) {
						s = getLogStudent(username, password);
					}
					openGUI(selectedUloga, s);
				} else {
					JOptionPane.showMessageDialog(Login2.this, "Pogrešno korisničko ime ili lozinka!", "Greška",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		add(ulogeLabel);
		add(ulogeComboBox);
		add(usernameLabel);
		add(usernameField);
		add(passwordLabel);
		add(passwordField);
		add(emptyLabel1);
		add(new JLabel()); // Prazan prostor
		add(emptyLabel2);
		add(loginButton);

		setVisible(true);

	}

	private Student getLogStudent(String username, String password) {
		final EntityManagerFactory emf = Persistence.createEntityManagerFactory("sistem_za_upis_akademske_godine");

		EntityManager em = emf.createEntityManager();
		try {
			Student student = em
					.createQuery("SELECT s FROM Student s WHERE s.username = :username AND s.password = :password",
							Student.class)
					.setParameter("username", username).setParameter("password", password).getSingleResult();
			return student;
		} catch (Exception e) {
			System.out.println("Greska" + " " + e);
			return null;
		}
	}

	private void openGUI(String uloga, Student s) {
		dispose(); // Zatvori trenutni prozor

		if (uloga.equals("Student")) {
			openStudentGUI(s);
		} else if (uloga.equals("Nastavnik")) {
			openNastavnikGUI();
		} else if (uloga.equals("Prodekan")) {
			openProdekanGUI();
		} else if (uloga.equals("Administrator")) {
			openAdministratorGUI();
		}
	}

	private void openStudentGUI(Student s) {
		JFrame studentFrame = new JFrame("Student GUI");
		studentFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		studentFrame.setSize(800, 400);

		StudentService studentService=new StudentService(usernameField.getText());
		

		JMenuBar menuBar = new JMenuBar();
		// Dodavanje akcije za odjavu
		JMenuItem odjavaMenuItem = new JMenuItem("Odjava");
		odjavaMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Akcija za odjavu
				JOptionPane.showMessageDialog(studentFrame, "Odjavljeni ste!", "Info", JOptionPane.INFORMATION_MESSAGE);
         		studentFrame.dispose(); // Zatvori prozor prodekana
				new Login2(); // Prikaz prozora prijave
			}
		});
		
		
		
		menuBar.add(odjavaMenuItem);
		studentFrame.setJMenuBar(menuBar);
	if(studentService.getStudent().getStatus()!=null) {
      if(studentService.getStudent().getStatus().equals("diplomirao")){
	  JOptionPane.showMessageDialog(studentFrame, "Vi ste diplomirali", "Info", JOptionPane.INFORMATION_MESSAGE);

		odjavaMenuItem.doClick();	
		}
		
	}
		
		JPanel registracijaPanel=new JPanel(new GridLayout(1,3));
		
		
		JPanel predmeti_akademskeGodinePanel=new JPanel(new BorderLayout()); 
		
		JLabel novaAkademskaGodinaLabel=new JLabel();
		JTextField searchField = new JTextField(20);
		PromptSupport.setPrompt("Pretrazujte...", searchField);
		PromptSupport.setFocusBehavior(FocusBehavior.SHOW_PROMPT, searchField);
		
		JPanel searchPanel=new JPanel(new GridLayout(2,1));
		searchPanel.add(novaAkademskaGodinaLabel);
		searchPanel.add(searchField);
		
	
		AkademskaGodina akGod=studentService.getNova();
		predmeti_akademskeGodinePanel.add(searchPanel,BorderLayout.NORTH);
		
		
		DefaultListModel<AkademskiPredmet> ljetniPredmetiModel=new DefaultListModel<>();
		if(akGod!=null) {
			ljetniPredmetiModel.addAll(akGod.ljetniSemestar());		}
		JList<AkademskiPredmet>  ljetniPredmetiList=new JList<>(ljetniPredmetiModel);
		JScrollPane ljetniPredmetiScroll=new JScrollPane(ljetniPredmetiList);
		
		JPanel ljetniPanel=new JPanel(new BorderLayout());
		JLabel ljetniLabel=new JLabel("Ljetni");
		
		ljetniPanel.add(ljetniLabel,BorderLayout.NORTH);
		ljetniPanel.add(ljetniPredmetiScroll,BorderLayout.CENTER);
		
	    JPanel zimskiPanel=new JPanel(new BorderLayout());
	    JLabel zimskiLabel=new JLabel("Zimski");
	    
		DefaultListModel<AkademskiPredmet> predmetiAkademskeGodineModel=new DefaultListModel<>();
		if(akGod!=null) {
			predmetiAkademskeGodineModel.addAll(akGod.zimskiSemestar());
		}
		JList<AkademskiPredmet>  predmetiAkademskeGodineList=new JList<>(predmetiAkademskeGodineModel);
		JScrollPane predmetiAkademskiGodineScroll=new JScrollPane(predmetiAkademskeGodineList);
		
		predmetiAkademskeGodineList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                	ljetniPredmetiList.clearSelection(); // Poništava selekciju u drugoj JList
                }
            }
        });
		
		ljetniPredmetiList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                	predmetiAkademskeGodineList.clearSelection(); // Poništava selekciju u drugoj JList
                }
            }
        });
		
		zimskiPanel.add(zimskiLabel,BorderLayout.NORTH);
		zimskiPanel.add(predmetiAkademskiGodineScroll,BorderLayout.CENTER);
		
		
		JPanel ljetniZimskiPanel=new JPanel(new GridLayout(2,1));
		ljetniZimskiPanel.add(zimskiPanel);
		ljetniZimskiPanel.add(ljetniPanel);
        
		

		searchField.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				updateList();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				updateList();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				updateList();
			}

			private void updateList() {
				String searchText = searchField.getText().toLowerCase();
				DefaultListModel<AkademskiPredmet> filteredModel = new DefaultListModel<>();
				DefaultListModel<AkademskiPredmet> filteredModel2=new DefaultListModel<>();

				for (int i = 0; i < predmetiAkademskeGodineModel.size(); i++) {
					 AkademskiPredmet item = predmetiAkademskeGodineModel.getElementAt(i);
					if (item.toString().toLowerCase().contains(searchText)) {
						filteredModel.addElement(item);
					}
				}
				
				for (int i = 0; i <  ljetniPredmetiModel.size(); i++) {
					 AkademskiPredmet item =  ljetniPredmetiModel.getElementAt(i);
					if (item.toString().toLowerCase().contains(searchText)) {
						filteredModel2.addElement(item);
					}
				}
				
				

				predmetiAkademskeGodineList.setModel(filteredModel);
				ljetniPredmetiList.setModel(filteredModel2);
			}
		});
		
		
		
		
		
		predmeti_akademskeGodinePanel.add(ljetniZimskiPanel,BorderLayout.CENTER);
		//predmeti_akademskeGodinePanel.add(searchField,BorderLayout.SOUTH);
    
		
        	
		JPanel buttonsPanel=new JPanel(new GridLayout(4,1));
		JButton dodajButton=new JButton("Dodaj");
		JButton izbrisiButton=new JButton("Izbrisi");
		JButton zahtjevButton=new JButton("Zahtjev za slusanje");
		JButton zakljucajButton=new JButton("Zakljucaj");
		
		//JPanel promjenaPredmetaPanel=new JPanel(new BorderLayout());
		
		
		
		
		
		JLabel zahtjevPromjenaLabel=new JLabel("Zahtjev za promjenu");
		JLabel prazanLabel=new JLabel("");
		JLabel izbaciLabel=new JLabel("Izbaci");
		JLabel ubaciLabel=new JLabel("Ubaci");
		JTextField searchFieldIzbaci = new JTextField(20);
		JTextField searchFieldUbaci = new JTextField(20);
		JPanel labelPanel=new JPanel(new GridLayout(3,3));
		labelPanel.add(zahtjevPromjenaLabel);
		labelPanel.add(prazanLabel);
		labelPanel.add(izbaciLabel);
		labelPanel.add(ubaciLabel);
		labelPanel.add(searchFieldIzbaci);
		labelPanel.add(searchFieldUbaci);
		

		
		JPanel odaberipredmetePanel=new JPanel(new GridLayout(3,2));
		
	
		
		DefaultListModel<AkademskiPredmet> predmetiAkademskeGodineModel2=new DefaultListModel<>();
		
		JList<AkademskiPredmet>  predmetiAkademskeGodineList2=new JList<>(predmetiAkademskeGodineModel2);
		JScrollPane predmetiAkademskiGodineScroll2=new JScrollPane(predmetiAkademskeGodineList2);
		
		DefaultListModel<AkademskiPredmet> predmetiAkademskeGodineModel3=new DefaultListModel<>();
		
		JList<AkademskiPredmet>  predmetiAkademskeGodineList3=new JList<>(predmetiAkademskeGodineModel3);
		JScrollPane predmetiAkademskiGodineScroll3=new JScrollPane(predmetiAkademskeGodineList3);
		
		
		Timer searchTimer = new Timer(300, new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		        String searchText = searchFieldIzbaci.getText();
		        List<AkademskiPredmet> searchResults = studentService.searchPredmeti(searchText, akGod.getId());
		        predmetiAkademskeGodineModel2.clear();
		        for (AkademskiPredmet predmet : searchResults) {
		           
		                predmetiAkademskeGodineModel2.addElement(predmet);
		            
		        }
		    }
		});
		searchTimer.setRepeats(false); // Postavite na false ako želite da se tajmer izvrši samo jednom

		searchFieldIzbaci.getDocument().addDocumentListener(new DocumentListener() {
		    @Override
		    public void insertUpdate(DocumentEvent e) {
		        restartTimer();
		    }

		    @Override
		    public void removeUpdate(DocumentEvent e) {
		        restartTimer();
		    }

		    @Override
		    public void changedUpdate(DocumentEvent e) {
		        restartTimer();
		    }

		    private void restartTimer() {
		    	
		    	 if (searchFieldIzbaci.getText().isEmpty()) {
		             predmetiAkademskeGodineModel2.clear(); // Ispraznite listu ako je tekst prazan
		             return;
		         }
		        if (searchTimer.isRunning()) {
		            searchTimer.restart(); // Ponovo pokreni tajmer ako već radi
		        } else {
		            searchTimer.start(); // Pokreni tajmer ako nije već pokrenut
		        }
		    }
		});
		
		
		Timer searchTimer2 = new Timer(300, new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		        String searchText = searchFieldUbaci.getText();
		        List<AkademskiPredmet> searchResults = studentService.searchPredmeti(searchText, akGod.getId());
		        predmetiAkademskeGodineModel3.clear();
		        for (AkademskiPredmet predmet : searchResults) {
		           
		                predmetiAkademskeGodineModel3.addElement(predmet);
		            
		        }
		    }
		});
		searchTimer2.setRepeats(false);

		searchFieldUbaci.getDocument().addDocumentListener(new DocumentListener() {
		    @Override
		    public void insertUpdate(DocumentEvent e) {
		        restartTimer();
		    }

		    @Override
		    public void removeUpdate(DocumentEvent e) {
		        restartTimer();
		    }

		    @Override
		    public void changedUpdate(DocumentEvent e) {
		        restartTimer();
		    }

		    private void restartTimer() {
		    	
		    	 if (searchFieldUbaci.getText().isEmpty()) {
		             predmetiAkademskeGodineModel3.clear(); // Ispraznite listu ako je tekst prazan
		             return;
		         }
		        if (searchTimer2.isRunning()) {
		            searchTimer2.restart(); // Ponovo pokreni tajmer ako već radi
		        } else {
		            searchTimer2.start(); // Pokreni tajmer ako nije već pokrenut
		        }
		    }
		});
		
		
		
		
		
		
		
		odaberipredmetePanel.add(predmetiAkademskiGodineScroll2);
		odaberipredmetePanel.add(predmetiAkademskiGodineScroll3);
		
		JTextField obrazlozenjeTextField=new JTextField();
		JLabel obrazlozenjeLabel=new JLabel("Obrazlozenje");
		
		odaberipredmetePanel.add(obrazlozenjeLabel);
		odaberipredmetePanel.add(obrazlozenjeTextField);
		


		
		JButton zahtjevPromjenaButton=new JButton("Promjeni predmet");
		odaberipredmetePanel.add(zahtjevPromjenaButton);
		
		
		JPanel odaberipredmetePanel2=new JPanel(new BorderLayout());
		odaberipredmetePanel2.add(labelPanel,BorderLayout.NORTH);
		odaberipredmetePanel2.add(odaberipredmetePanel,BorderLayout.CENTER);
		
		buttonsPanel.add(dodajButton);
		buttonsPanel.add(izbrisiButton);
		buttonsPanel.add(zahtjevButton);
		buttonsPanel.add(zakljucajButton);

		JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, buttonsPanel, odaberipredmetePanel2);
		splitPane.setResizeWeight(0.5); 
         
		
		
				
	
			
		
		
		
	
		
		
		
		
		

		
		
		if(akGod==null) {
			dodajButton.setEnabled(false);
			zakljucajButton.setEnabled(false);
			izbrisiButton.setEnabled(false);
			zahtjevButton.setEnabled(false);
			zahtjevPromjenaButton.setEnabled(false);
			novaAkademskaGodinaLabel.setText("Nije definirana nova akademska godina");
		
		}else {
			
			novaAkademskaGodinaLabel.setText(akGod.toString());
			studentService.obradiAkademskuGodinu(akGod);
			AkademskaGodina ak=studentService.getNova();
			Student s2=studentService.findStudent(usernameField.getText());

			if(ak.getStudenti().contains(studentService.findStudent(usernameField.getText()))) {
				
				if(s2.getGodinaStudija().getNazivGodineStudija().equals("prva")&& s2.getStatus().equals("prvi puta")) {
					 JOptionPane.showMessageDialog(studentFrame, "Upisani ste u prvu godinu studija prvi puta", "Greska",
								JOptionPane.INFORMATION_MESSAGE);	
					dodajButton.setEnabled(false);
					zakljucajButton.setEnabled(false);
					izbrisiButton.setEnabled(false);
					zahtjevButton.setEnabled(false);
					zahtjevPromjenaButton.setEnabled(false);
				}else {
				
				
				dodajButton.setEnabled(false);
				zakljucajButton.setEnabled(false);
				izbrisiButton.setEnabled(false);
				zahtjevButton.setEnabled(false);
				zahtjevPromjenaButton.setEnabled(true);
				}
			}else {
               
			if(!akGod.upisOtvoren()) {
				dodajButton.setEnabled(false);
				zakljucajButton.setEnabled(false);
				izbrisiButton.setEnabled(false);
				zahtjevButton.setEnabled(false);
				zahtjevPromjenaButton.setEnabled(false);
				
			}else {
			
				zahtjevPromjenaButton.setEnabled(false);
				dodajButton.setEnabled(true);
				zakljucajButton.setEnabled(true);
				izbrisiButton.setEnabled(true);
				zahtjevButton.setEnabled(false);
				
				
				
			}
			
			}
		}
		
		
JPanel studentPredmetPanel=new JPanel(new BorderLayout());		
		
		JLabel studentPredmetLabel=new JLabel("Predmeti za novu akademsku godinu");
		
		DefaultListModel<AkademskiPredmet> akademskiPredmetModel=new DefaultListModel<>();
		List<AkademskiPredmet> akademskiPredmetiStudenta=studentService.findStudent(usernameField.getText()).getPredmeti_ak_godine();
		for(AkademskiPredmet p:akademskiPredmetiStudenta) {
			if(akGod!=null) {
			if(p.getAkademskaGodina().getId()==akGod.getId())
				akademskiPredmetModel.addElement(p);
		
		}
		}
		
		
		
		JList<AkademskiPredmet> akademskiPredmetList=new JList<>(akademskiPredmetModel);
		JScrollPane akademskiPredmetScroll=new JScrollPane(akademskiPredmetList);
		
		
		
		
		studentPredmetPanel.add(studentPredmetLabel,BorderLayout.NORTH);
		studentPredmetPanel.add(akademskiPredmetScroll,BorderLayout.CENTER);
		
		
	
		zahtjevPromjenaButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
                
				AkademskiPredmet zaIzbacit=predmetiAkademskeGodineList2.getSelectedValue();
				AkademskiPredmet zaUbacit=predmetiAkademskeGodineList3.getSelectedValue();
				if(zaIzbacit!=null&&zaUbacit!=null) {
			//	akGod=studentService.getTrenutna();
					AkademskaGodina ak2=studentService.getNova();
					
					studentService.zahtjevPromjena(zaIzbacit,zaUbacit,obrazlozenjeTextField.getText(),ak2);
				
				
				}else {
					JOptionPane.showMessageDialog(studentFrame, "Selektujte predmete!", "Greska",
							JOptionPane.ERROR_MESSAGE);
					
				}
				
			}
		});
		
		
		dodajButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
		    
			
			AkademskiPredmet p3=predmetiAkademskeGodineList.getSelectedValue();
			AkademskiPredmet p4=ljetniPredmetiList.getSelectedValue();
			AkademskiPredmet p=new AkademskiPredmet();
			if(p3==null)
				p=p4;
			else
				p=p3;
			
			if(p!=null) {
			for(PredmetOcjena p2:studentService.findStudent(usernameField.getText()).getOcjene()){
				if(p2.getAkademskiPredmet().getPredmet().getSifra_predmeta().equals(p.getPredmet().getSifra_predmeta())){
					if(p2.getOcjena()>=6) {
						JOptionPane.showMessageDialog(studentFrame, "Predmet ste vec polozili!", "Greska",
								JOptionPane.ERROR_MESSAGE);
						return;
					}
				}
			}}
			
			
			
              if(p!=null) {	
            	  if(!akademskiPredmetModel.contains(p)) {
			             if(studentService.provjeraPreduslova(p)||studentService.zahtjevOdobren(p)) {
							akademskiPredmetModel.addElement(p);
							//studentService.dodajPredmetStudentu(p);
						
						    }else {
						    	JOptionPane.showMessageDialog(studentFrame, "Nemate ispunjene preduslove. Posaljite zahtjev nastavniku!", "Greska",
										JOptionPane.ERROR_MESSAGE);
						    }
            	  
              }else {
            	  JOptionPane.showMessageDialog(studentFrame, "Imate vec unesen predmet", "Greska",
							JOptionPane.ERROR_MESSAGE);
              }
			
				}
			
			
			
			
			}});
		
		izbrisiButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				AkademskiPredmet p3=predmetiAkademskeGodineList.getSelectedValue();
			
			   
				if(p3!=null&&!(studentService.findStudent(usernameField.getText()).getPredmeti_ak_godine().contains(p3))) {
					akademskiPredmetModel.remove(akademskiPredmetList.getSelectedIndex());
				  // studentService.izbrisiPredmetStudentu(p);
				}else {
					JOptionPane.showMessageDialog(studentFrame, "Predmet koji pokusavate izbrisati morate slusati!", "Greska",
							JOptionPane.INFORMATION_MESSAGE);
				}
					
				}
		});
		
	/*	zahtjevButton.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent a) {
		        AkademskiPredmet p = predmetiAkademskeGodineList.getSelectedValue();
		        ZahtjevPreduslov z = new ZahtjevPreduslov();
		        
		        if (p != null) {
		            String obrazlozenje = JOptionPane.showInputDialog(null, "Unesite obrazloženje:", "Unos obrazloženja", JOptionPane.PLAIN_MESSAGE);
		            if (obrazlozenje != null) {
		                z.setObrazlozenje(obrazlozenje);
		                studentService.addZahtjevPreduslov(z, p);
		            }else {
		            	 z.setObrazlozenje("");
			                studentService.addZahtjevPreduslov(z, p);
		            }
		        }
		    }
		});

		*/
		
		zakljucajButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent a) {
			
				
				
				List<AkademskiPredmet> predmeti=new ArrayList<>();
				
				int ljetni=0;
				int zimski=0;
				for(int i=0;i<akademskiPredmetModel.getSize();++i) {
					predmeti.add(akademskiPredmetModel.getElementAt(i));
					if(akademskiPredmetModel.getElementAt(i).getPredmet().getSemestar().getLjetniZimski().equals("ljetni"))
						++ljetni;
					else 
						++zimski;
				}
				
				AkademskaGodina akGod2=studentService.getNova();
				
				
				if(studentService.findStudent(usernameField.getText()).getEcts()<168) {
					if(predmeti.size()==10&&ljetni==5&&zimski==5) {
					
						studentService.dodajStudentaAkademskojGodini(predmeti,akGod2);
				        
						dodajButton.setEnabled(false);
						zakljucajButton.setEnabled(false);
						izbrisiButton.setEnabled(false);
						zahtjevButton.setEnabled(false);
						zahtjevPromjenaButton.setEnabled(true);
					}else {
						JOptionPane.showMessageDialog(studentFrame, "Morate imati 10 predmeta (5 zimski + 5 ljetni)!", "Greska",
								JOptionPane.INFORMATION_MESSAGE);
					}
				}else {
					
					if(predmeti.size()<=12) {
					studentService.dodajStudentaAkademskojGodini(predmeti,akGod2);
			        
					dodajButton.setEnabled(false);
					zakljucajButton.setEnabled(false);
					izbrisiButton.setEnabled(false);
					zahtjevButton.setEnabled(false);
					zahtjevPromjenaButton.setEnabled(true);
					}else {
						JOptionPane.showMessageDialog(studentFrame, "Mozete imati maksimalno 12 predmeta !", "Greska",
								JOptionPane.INFORMATION_MESSAGE);
					}
				}
				
				
			        
			        
			        
			
			
			
			}});	
	
	
		
		
		registracijaPanel.add(predmeti_akademskeGodinePanel);
		registracijaPanel.add(splitPane);
		registracijaPanel.add(studentPredmetPanel);
		
		
		
		JPanel zahtjevPanel=new JPanel(new GridLayout(1,2));
		
		JPanel zahtjevPromjenaPanel=new JPanel(new BorderLayout());
		JPanel zahtjevPreduslovPanel=new JPanel(new BorderLayout());
		JPanel zahtjevPredispitniPanel=new JPanel(new BorderLayout());
		
		JLabel zahtjevPreduslovLabel=new JLabel("Zahtjevi(preduslovi)");
		
		DefaultListModel<ZahtjevPreduslov> zahtjevPreduslovModel=new DefaultListModel<>();
		JList<ZahtjevPreduslov> zahtjevPreduslovList=new JList<>(zahtjevPreduslovModel);
		JScrollPane zahtjevPreduslovScrollPane=new JScrollPane(zahtjevPreduslovList);
		JButton zahtjevPreduslovButton=new JButton("Pregledaj");
		
		JButton izbrisiZahtjev=new JButton("Izbrisi");
		JButton zahtjevDetalji=new JButton("Detalji");
		
		JPanel zahtjevButtons=new JPanel(new GridLayout(1,3));
	
		zahtjevButtons.add(zahtjevPreduslovButton);
		zahtjevButtons.add(izbrisiZahtjev);
		zahtjevButtons.add(zahtjevDetalji);
		
		izbrisiZahtjev.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ZahtjevPreduslov z= zahtjevPreduslovList.getSelectedValue();
				if(z!=null) {
				     zahtjevPreduslovModel.clear();
					studentService.izbrisiZahtjevZaPreduslov(z);
				}
			}
		});
		
		zahtjevDetalji.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ZahtjevPreduslov z= zahtjevPreduslovList.getSelectedValue();
				if(z!=null) {
					String detalji = "Detalji iz zahtjeva:\n"
		                    + "Student: " + z.getStudent().toString() + "\n"
		                    //+ "Predmet: " + z.getPredmet().getNaziv() + "\n"
		                    + "Obrazloženje: " + z.getObrazlozenje();

		            JOptionPane.showMessageDialog(
		                    studentFrame,
		                    detalji,
		                    "Detalji iz zahtjeva",
		                    JOptionPane.INFORMATION_MESSAGE
		            );
					
				}
			}
		});
		
		 
		
		zahtjevPreduslovButton.addActionListener(new ActionListener() {
			
			
			public void actionPerformed(ActionEvent e) {
				zahtjevPreduslovModel.clear();
				List<ZahtjevPreduslov> zahtjevi=studentService.zahtjevPreduslovi();
				zahtjevPreduslovModel.addAll(zahtjevi);
				
				
			}
		});
		
		zahtjevPreduslovPanel.add(zahtjevPreduslovLabel,BorderLayout.NORTH);
		zahtjevPreduslovPanel.add(zahtjevButtons,BorderLayout.SOUTH);
		zahtjevPreduslovPanel.add(zahtjevPreduslovScrollPane,BorderLayout.CENTER);
		
		
		
        JLabel zahtjevPromjenaLabel2=new JLabel("Promjene predmeta");
		
		DefaultListModel<Zahtjev> zahtjevPromjenaModel=new DefaultListModel<>();
		JList<Zahtjev> zahtjevPromjenaList=new JList<>(zahtjevPromjenaModel);
		JScrollPane zahtjevPromjenaScrollPane=new JScrollPane(zahtjevPromjenaList);
		JButton zahtjevPromjenaButton2=new JButton("Pregledaj");
		JButton izbrisiZahtjev2=new JButton("Izbrisi");
		JButton zahtjevDetalji2=new JButton("Detalji");
		
		JPanel zahtjevButtons2=new JPanel(new GridLayout(1,3));
	
		zahtjevButtons2.add(zahtjevPromjenaButton2);
		zahtjevButtons2.add(izbrisiZahtjev2);
		zahtjevButtons2.add(zahtjevDetalji2);
		zahtjevDetalji2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Zahtjev z= zahtjevPromjenaList.getSelectedValue();
				if(z!=null) {
					String detalji = "Detalji iz zahtjeva:\n"
		                    + "Student: " + z.getStudent().toString() + "\n"
		                    + "Izbacuje:"  + z.getTrenutni().toString() + "\n"
		                     + "Ubacuje:"  + z.getZamjenski().toString() + "\n"
		                    + "Obrazloženje: " + z.getObrazlozenje();

		            JOptionPane.showMessageDialog(
		                    null,
		                    detalji,
		                    "Detalji iz zahtjeva",
		                    JOptionPane.INFORMATION_MESSAGE
		            );
					
				}
			}
		});
		
		izbrisiZahtjev2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Zahtjev z= zahtjevPromjenaList.getSelectedValue();
				if(z!=null) {
					zahtjevPromjenaModel.remove(zahtjevPromjenaList.getSelectedIndex());
					studentService.izbrisiZahtjevZaPromjenu(z);
					
				}
			}
		});
		zahtjevPromjenaButton2.addActionListener(new ActionListener() {
			
			
			public void actionPerformed(ActionEvent e) {
				zahtjevPromjenaModel.clear();
				List<Zahtjev> zahtjevi=studentService.zahtjeviZaPromjenuPredmeta();
				zahtjevPromjenaModel.addAll(zahtjevi);
				
				
			}
		});
		
		zahtjevPromjenaPanel.add(zahtjevPromjenaLabel2,BorderLayout.NORTH);
		zahtjevPromjenaPanel.add(zahtjevButtons2,BorderLayout.SOUTH);
		zahtjevPromjenaPanel.add(zahtjevPromjenaScrollPane,BorderLayout.CENTER);
		
		
		
		
		
		
		
		
		zahtjevPanel.add(zahtjevPreduslovPanel);
		zahtjevPanel.add(zahtjevPromjenaPanel);
		
		JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.addTab("Registracija predmeta", registracijaPanel);
		tabbedPane.addTab(" Student zahtjevi", zahtjevPanel);
		
		
		
		
		
		studentFrame.add(tabbedPane);
		
		studentFrame.setVisible(true);
		
	
	}

	private void openNastavnikGUI() {
		JFrame nastavnikFrame = new JFrame("Nastavnik GUI");
		nastavnikFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		nastavnikFrame.setSize(800, 500);
		nastavnikFrame.setVisible(true);
		NastavnikService nastavnikService = new NastavnikService(usernameField.getText());
		// ProdekanService prodekanService=new ProdekanService("emir.meskovic");

		// JPanel mainPanel = new JPanel(new BorderLayout());

		JMenuBar menuBar = new JMenuBar();
		// Dodavanje akcije za odjavu
		JMenuItem odjavaMenuItem = new JMenuItem("Odjava");
		odjavaMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Akcija za odjavu
				JOptionPane.showMessageDialog(nastavnikFrame, "Odjavljeni ste!", "Info",
						JOptionPane.INFORMATION_MESSAGE);
				nastavnikFrame.dispose(); // Zatvori prozor prodekana
				new Login2(); // Prikaz prozora prijave
			}
		});
		menuBar.add(odjavaMenuItem);
		nastavnikFrame.setJMenuBar(menuBar);

		JPanel trenutnaAkademskaGodinaPanel = new JPanel(new GridLayout(1, 3));
		JPanel akademskiPredmetPanel = new JPanel(new BorderLayout());

		DefaultListModel<AkademskiPredmet> akademskiPredmetModel = new DefaultListModel<>();
		AkademskaGodina ak = nastavnikService.getTrenutna();
		System.out.println(ak);
		if (ak != null) {
			List<AkademskiPredmet> predmetiPredavaca = nastavnikService.getPredmeti(ak);
			akademskiPredmetModel.addAll(predmetiPredavaca);
		}
		JList<AkademskiPredmet> akademskiPredmetList = new JList<>(akademskiPredmetModel);
		JScrollPane akademskiPredmetScrollPane = new JScrollPane(akademskiPredmetList);

		JTextField searchField = new JTextField(20);
		PromptSupport.setPrompt("Pretrazujte...", searchField);
		PromptSupport.setFocusBehavior(FocusBehavior.SHOW_PROMPT, searchField);

		searchField.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				updateList();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				updateList();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				updateList();
			}

			private void updateList() {
				String searchText = searchField.getText().toLowerCase();
				DefaultListModel<AkademskiPredmet> filteredModel = new DefaultListModel<>();

				for (int i = 0; i < akademskiPredmetModel.size(); i++) {
					AkademskiPredmet item = akademskiPredmetModel.getElementAt(i);
					if (item.toString().toLowerCase().contains(searchText)) {
						filteredModel.addElement(item);
					}
				}

				akademskiPredmetList.setModel(filteredModel);
			}
		});

		akademskiPredmetPanel.add(searchField, BorderLayout.NORTH);
		akademskiPredmetPanel.add(akademskiPredmetScrollPane, BorderLayout.CENTER);

		JPanel studentPanel = new JPanel(new BorderLayout());

		DefaultListModel<Student> studentModel = new DefaultListModel<>();

		JList<Student> studentList = new JList<>(studentModel);
		JScrollPane studentScrollPane = new JScrollPane(studentList);
		studentPanel.add(studentScrollPane, BorderLayout.CENTER);

		JPanel buttons = new JPanel(new GridLayout(1, 2));
		JButton ocjenaButton = new JButton("Ocjena");
		JButton pregledajStudenteButton = new JButton("Pregledaj studente");

		// ...
		pregledajStudenteButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				AkademskiPredmet p = akademskiPredmetList.getSelectedValue();
				AkademskiPredmet p2=nastavnikService.mergePredmet(p);
				if (p != null) {
					studentModel.clear();
					studentModel.addAll(p2.getStudenti());
					System.out.println(p.getStudenti());
					System.out.println(p.getStudenti().size());
				} else {
					JOptionPane.showMessageDialog(nastavnikFrame, "Selektujte predmet!", "Greska",
							JOptionPane.ERROR_MESSAGE);
				}
			}

		});
		
		ocjenaButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				AkademskiPredmet p = akademskiPredmetList.getSelectedValue();
				Student s = studentList.getSelectedValue();
				boolean flag = true;

				if (s != null) {
					List<PredmetOcjena> chkPredOcj = s.getOcjene();
					for (PredmetOcjena prOcj : chkPredOcj) {

						if (prOcj.getAkademskiPredmet().getId() == p.getId()) {
							JOptionPane.showMessageDialog(nastavnikFrame, "Za predmet je vec unesena ocjena", "Info",
									JOptionPane.INFORMATION_MESSAGE);
							flag = false;
							break;
						} else {
							flag = true;
						}
					}
				}

				if (flag) {
					if (p != null || s != null) {
						System.out.println(p);
						System.out.println(s);
						Student azurirani=showOcjenaPopup(p, s);
						studentModel.setElementAt(azurirani, studentList.getSelectedIndex());
						
					} else {
						JOptionPane.showMessageDialog(nastavnikFrame, "Selektujte studenta i predmet!", "Greska",
								JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});

		buttons.add(pregledajStudenteButton);
		buttons.add(ocjenaButton);
		studentPanel.add(buttons, BorderLayout.SOUTH);

		
	

		JPanel zahtjeviPanel = new JPanel(new BorderLayout());
		DefaultListModel<PredmetOcjena> studentModelOcjena = new DefaultListModel<>();

		JList<PredmetOcjena> studentListOcjena = new JList<>(studentModelOcjena);
		JScrollPane studentScrollPaneOcjena = new JScrollPane(studentListOcjena);
		zahtjeviPanel.add(studentScrollPaneOcjena, BorderLayout.CENTER);

		JButton pregledajOcjeneButton = new JButton("Pregledaj ocjene");
		pregledajOcjeneButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				studentModelOcjena.clear();

				Student s = studentList.getSelectedValue();
				if (s != null) {
					
					studentModelOcjena.addAll(s.getOcjene());
				} else {
					JOptionPane.showMessageDialog(nastavnikFrame, "Selektujte studenta!", "Greska",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		zahtjeviPanel.add(pregledajOcjeneButton, BorderLayout.SOUTH);

		trenutnaAkademskaGodinaPanel.add(akademskiPredmetPanel);
		trenutnaAkademskaGodinaPanel.add(studentPanel);
		trenutnaAkademskaGodinaPanel.add(zahtjeviPanel);

		/*JPanel novaAkGodinaPanel = new JPanel(new GridLayout(1, 4));

		JPanel saglasnostPanel = new JPanel(new BorderLayout());
		DefaultListModel<ZahtjevBodovi> saglasnostPanelModel = new DefaultListModel<>();
		JList<ZahtjevBodovi> saglasnostPanelList = new JList<>(saglasnostPanelModel);
		JScrollPane saglasnostPanelScrollPane = new JScrollPane(saglasnostPanelList);
		JButton saglasnostBod = new JButton("Pregledaj zahtjev");
		JLabel saglasnostLabel = new JLabel("Lista zahtjeva za prenos bodova!");

		JPanel saglasnostPanelPreduslov = new JPanel(new BorderLayout());
		DefaultListModel<ZahtjevPreduslov> saglasnostPanelModelPreduslov = new DefaultListModel<>();
		JList<ZahtjevPreduslov> saglasnostPanelPreduslovList = new JList<>(saglasnostPanelModelPreduslov);
		JScrollPane saglasnostPanelScrollPanePreduslov = new JScrollPane(saglasnostPanelPreduslovList);
		JLabel saglasnostLabelPreduslov = new JLabel("Lista zahtjeva za preduslov");
		JButton saglasnostPred = new JButton("Pregledaj zahtjev");

		JPanel pregledUspjehaPreduslovPanel = new JPanel(new BorderLayout());
		DefaultListModel<PredmetOcjena> pregledUspjehaPreduslovPanelModel = new DefaultListModel<>();
		JList<PredmetOcjena> pregledUspjehaPreduslovPanelList = new JList<>(pregledUspjehaPreduslovPanelModel);
		JScrollPane pregledUspjehaScrollPanePreduslov = new JScrollPane(pregledUspjehaPreduslovPanelList);
		JLabel pregledUspjehaLabelPreduslov = new JLabel("Pregled uspjeha: ");
		JButton pregledUspjeha = new JButton("Pregledaj uspjeh studenta");

		saglasnostPanel.add(saglasnostPanelScrollPane, BorderLayout.CENTER);
		saglasnostPanel.add(saglasnostLabel, BorderLayout.NORTH);
		saglasnostPanel.add(saglasnostBod, BorderLayout.SOUTH);

		saglasnostPanelPreduslov.add(saglasnostPanelScrollPanePreduslov, BorderLayout.CENTER);
		saglasnostPanelPreduslov.add(saglasnostLabelPreduslov, BorderLayout.NORTH);
		saglasnostPanelPreduslov.add(saglasnostPred, BorderLayout.SOUTH);

		pregledUspjehaPreduslovPanel.add(pregledUspjehaScrollPanePreduslov, BorderLayout.CENTER);
		pregledUspjehaPreduslovPanel.add(pregledUspjehaLabelPreduslov, BorderLayout.NORTH);
		pregledUspjehaPreduslovPanel.add(pregledUspjeha, BorderLayout.SOUTH);

		JPanel studentPanel2 = new JPanel(new BorderLayout());

		DefaultListModel<Student> studentModel2 = new DefaultListModel<>();

		JList<Student> studentList2 = new JList<>(studentModel2);
		JScrollPane studentScrollPane2 = new JScrollPane(studentList2);
		studentPanel2.add(studentScrollPane2, BorderLayout.CENTER);

		JPanel akademskiPredmetPanel2 = new JPanel(new BorderLayout());

		DefaultListModel<AkademskiPredmet> akademskiPredmetModel2 = new DefaultListModel<>();
		AkademskaGodina ak2 = nastavnikService.getNova();
		if (ak2 != null) {
			List<AkademskiPredmet> predmetiPredavaca2 = nastavnikService.getPredmeti(ak2);
			akademskiPredmetModel2.addAll(predmetiPredavaca2);
		}
		JList<AkademskiPredmet> akademskiPredmetList2 = new JList<>(akademskiPredmetModel2);
		JScrollPane akademskiPredmetScrollPane2 = new JScrollPane(akademskiPredmetList2);

		JPanel buttons2 = new JPanel(new GridLayout(2, 2));
		JButton pregledajButton = new JButton("Studenti");
		JButton pregledajZahtjeveZaSaglasnost = new JButton("Zahtjevi za saglasnost");
		JButton pregledajZahtjeveZaPrenos = new JButton("Z");

		pregledajButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				AkademskiPredmet selectedPredmet = akademskiPredmetList2.getSelectedValue();
				if (selectedPredmet != null) {
					studentModel2.clear();
					studentModel2.addAll(selectedPredmet.getStudenti());

				} else {
					JOptionPane.showMessageDialog(nastavnikFrame, "Odaberite predmet iz liste!", "Greška",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		saglasnostPred.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				AkademskiPredmet selectedPredmet = akademskiPredmetList2.getSelectedValue();
				if (selectedPredmet != null) {
					saglasnostPanelModel.clear();
					if (selectedPredmet.getZahtBod().size() != 0) {

						for (ZahtjevBodovi zb : selectedPredmet.getZahtBod()) {
							if (zb.getStatus().equals("Na cekanju.")) {
								saglasnostPanelModel.addElement(zb);

							}
						}
					}
//					saglasnostPanelModel.addAll(selectedPredmet.getZahtBod());
					System.out.println(selectedPredmet.getZahtBod());
				} else {
					JOptionPane.showMessageDialog(nastavnikFrame, "Odaberite predmet iz liste!", "Greška",
							JOptionPane.ERROR_MESSAGE);
				}
			}

		});

		pregledajZahtjeveZaPrenos.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				AkademskiPredmet selectedPredmet = akademskiPredmetList2.getSelectedValue();
				if (selectedPredmet != null) {
					saglasnostPanelModelPreduslov.clear();
					if (selectedPredmet.getZahtPred().size() != 0) {
						for (ZahtjevPreduslov zb : selectedPredmet.getZahtPred()) {
							if (zb.getStatus().equals("Na cekanju.")) {
								saglasnostPanelModelPreduslov.addElement(zb);
							}
						}
					}
//					saglasnostPanelModel.addAll(selectedPredmet.getZahtBod());
					System.out.println(selectedPredmet.getZahtBod());
				} else {
					JOptionPane.showMessageDialog(nastavnikFrame, "Odaberite predmet iz liste!", "Greška",
							JOptionPane.ERROR_MESSAGE);
				}
			}

		});

		saglasnostBod.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ZahtjevBodovi selectedZaht = saglasnostPanelList.getSelectedValue();
				if (selectedZaht != null) {

					EntityManager em = emf.createEntityManager();
					em.getTransaction().begin();
					ZahtjevBodovi zB = em.merge(selectedZaht);

					int option = JOptionPane.showConfirmDialog(null,
							zB.getObrazlozenje() + "\nDa li zelite odobriti zahtjev", "Odobrenje zahtjeva",
							JOptionPane.YES_NO_OPTION);
					if (option == JOptionPane.YES_OPTION) {
						zB.setStatus("Odobren");
					} else if (option == JOptionPane.NO_OPTION) {
						zB.setStatus("Odbijen");
						JTextArea textArea = new JTextArea(10, 40); // Rows, Columns
						textArea.setText(" ");
						textArea.setLineWrap(true);
						textArea.setWrapStyleWord(true);

						JScrollPane scrollPane = new JScrollPane(textArea);
                        
						int option1 = JOptionPane.showConfirmDialog(null, scrollPane, "Unesite obrazlozenje",
								JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

						if (option1 == JOptionPane.OK_OPTION) {
							zB.setObrazlozenje(textArea.getText()); // Set the value of 'zaht' using the input
						}
						em.getTransaction().commit();
					}

				} else {
					JOptionPane.showMessageDialog(nastavnikFrame, "Odaberite zahtjev iz liste!", "Greška",
							JOptionPane.ERROR_MESSAGE);
				}
			}

		});

		buttons2.add(pregledajButton);
		buttons2.add(pregledajZahtjeveZaSaglasnost);
		buttons2.add(pregledajZahtjeveZaPrenos);

		JTextField searchField3 = new JTextField(20);
		PromptSupport.setPrompt("Pretrazujte...", searchField3);
		PromptSupport.setFocusBehavior(FocusBehavior.SHOW_PROMPT, searchField3);

		searchField3.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				updateList();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				updateList();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				updateList();
			}

			private void updateList() {
				String searchText = searchField3.getText().toLowerCase();
				DefaultListModel<AkademskiPredmet> filteredModel = new DefaultListModel<>();

				for (int i = 0; i < akademskiPredmetModel2.size(); i++) {
					AkademskiPredmet item = akademskiPredmetModel2.getElementAt(i);
					if (item.toString().toLowerCase().contains(searchText)) {
						filteredModel.addElement(item);
					}
				}

				akademskiPredmetList2.setModel(filteredModel);
			}
		});

		akademskiPredmetPanel2.add(searchField3, BorderLayout.NORTH);
		akademskiPredmetPanel2.add(akademskiPredmetScrollPane2, BorderLayout.CENTER);
		akademskiPredmetPanel2.add(buttons2, BorderLayout.SOUTH);
		
		novaAkGodinaPanel.add(akademskiPredmetPanel2);
		novaAkGodinaPanel.add(studentPanel2);
		novaAkGodinaPanel.add(saglasnostPanel);
		novaAkGodinaPanel.add(saglasnostPanel);
		novaAkGodinaPanel.add(saglasnostPanelPreduslov);
		novaAkGodinaPanel.add(pregledUspjehaPreduslovPanel);
        */
		// TabbedPane
		
		JPanel novaAkademskaGodinaPanel = new JPanel(new GridLayout(1, 3));
		JPanel akademskiPredmetPanel2 = new JPanel(new BorderLayout());

		DefaultListModel<AkademskiPredmet> akademskiPredmetModel2 = new DefaultListModel<>();
		AkademskaGodina ak2 = nastavnikService.getNova();
		System.out.println(ak2);
		if (ak != null) {
			List<AkademskiPredmet> predmetiPredavaca2 = nastavnikService.getPredmeti(ak);
			akademskiPredmetModel2.addAll(predmetiPredavaca2);
		}
		JList<AkademskiPredmet> akademskiPredmetList2 = new JList<>(akademskiPredmetModel2);
		JScrollPane akademskiPredmetScrollPane2 = new JScrollPane(akademskiPredmetList2);

		JTextField searchField2 = new JTextField(20);
		PromptSupport.setPrompt("Pretrazujte...", searchField2);
		PromptSupport.setFocusBehavior(FocusBehavior.SHOW_PROMPT, searchField2);

		searchField.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				updateList();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				updateList();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				updateList();
			}

			private void updateList() {
				String searchText = searchField.getText().toLowerCase();
				DefaultListModel<AkademskiPredmet> filteredModel = new DefaultListModel<>();

				for (int i = 0; i < akademskiPredmetModel.size(); i++) {
					AkademskiPredmet item = akademskiPredmetModel.getElementAt(i);
					if (item.toString().toLowerCase().contains(searchText)) {
						filteredModel.addElement(item);
					}
				}

				akademskiPredmetList2.setModel(filteredModel);
			}
		});

		akademskiPredmetPanel2.add(searchField2, BorderLayout.NORTH);
		akademskiPredmetPanel2.add(akademskiPredmetScrollPane2, BorderLayout.CENTER);

		JPanel studentPanel2 = new JPanel(new BorderLayout());

		DefaultListModel<Student> studentModel2 = new DefaultListModel<>();

		JList<Student> studentList2 = new JList<>(studentModel2);
		JScrollPane studentScrollPane2 = new JScrollPane(studentList2);
		studentPanel2.add(studentScrollPane2, BorderLayout.CENTER);

		JPanel buttons2 = new JPanel(new GridLayout(1, 2));
		JButton ocjenaButton2 = new JButton("Ocjena");
		JButton pregledajStudenteButton2 = new JButton("Pregledaj studente");

		// ...
		pregledajStudenteButton2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				AkademskiPredmet p = akademskiPredmetList2.getSelectedValue();
				AkademskiPredmet p2=nastavnikService.mergePredmet(p);
				if (p != null) {
					studentModel2.clear();
					studentModel2.addAll(p2.getStudenti());
					System.out.println(p.getStudenti());
					System.out.println(p.getStudenti().size());
				} else {
					JOptionPane.showMessageDialog(nastavnikFrame, "Selektujte predmet!", "Greska",
							JOptionPane.ERROR_MESSAGE);
				}
			}

		});
		
		ocjenaButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				AkademskiPredmet p = akademskiPredmetList.getSelectedValue();
				Student s = studentList.getSelectedValue();
				boolean flag = true;

				if (s != null) {
					List<PredmetOcjena> chkPredOcj = s.getOcjene();
					for (PredmetOcjena prOcj : chkPredOcj) {

						if (prOcj.getAkademskiPredmet().getId() == p.getId()) {
							JOptionPane.showMessageDialog(nastavnikFrame, "Za predmet je vec unesena ocjena", "Info",
									JOptionPane.INFORMATION_MESSAGE);
							flag = false;
							break;
						} else {
							flag = true;
						}
					}
				}

				if (flag) {
					if (p != null || s != null) {
						System.out.println(p);
						System.out.println(s);
						Student azurirani=showOcjenaPopup(p, s);
						studentModel.setElementAt(azurirani, studentList.getSelectedIndex());
						
					} else {
						JOptionPane.showMessageDialog(nastavnikFrame, "Selektujte studenta i predmet!", "Greska",
								JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});

		buttons.add(pregledajStudenteButton);
		buttons.add(ocjenaButton);
		studentPanel2.add(pregledajStudenteButton2, BorderLayout.SOUTH);

		
	

		JPanel ocjenePanel2 = new JPanel(new BorderLayout());
		DefaultListModel<PredmetOcjena> studentModelOcjena2 = new DefaultListModel<>();

		JList<PredmetOcjena> studentListOcjena2 = new JList<>(studentModelOcjena2);
		JScrollPane studentScrollPaneOcjena2 = new JScrollPane(studentListOcjena2);
		ocjenePanel2.add(studentScrollPaneOcjena2, BorderLayout.CENTER);

		JButton pregledajOcjeneButton2 = new JButton("Pregledaj ocjene");
		pregledajOcjeneButton2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				studentModelOcjena2.clear();

				Student s = studentList2.getSelectedValue();
				if (s != null) {
					
					studentModelOcjena2.addAll(s.getOcjene());
				} else {
					JOptionPane.showMessageDialog(nastavnikFrame, "Selektujte studenta!", "Greska",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		ocjenePanel2.add(pregledajOcjeneButton2, BorderLayout.SOUTH);

		novaAkademskaGodinaPanel.add(akademskiPredmetPanel2);
		novaAkademskaGodinaPanel.add(studentPanel2);
		novaAkademskaGodinaPanel.add(ocjenePanel2);
		JTabbedPane tabbedPane = new JTabbedPane();

		tabbedPane.addTab("Trenutna godina", trenutnaAkademskaGodinaPanel);
		tabbedPane.add("Nova akademska godina", novaAkademskaGodinaPanel);

		nastavnikFrame.add(tabbedPane);

		nastavnikFrame.setVisible(true);

	}

	private void openProdekanGUI() {
		JFrame prodekanFrame = new JFrame("Prodekan GUI");
		prodekanFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		prodekanFrame.setSize(800, 500); // Povećao sam veličinu prozora kako bi lista imala dovoljno prostora

		ProdekanService prodekanService = new ProdekanService(usernameField.getText());

		// Gornja traka s izbornicima
		JMenuBar menuBar = new JMenuBar();
		// Dodavanje akcije za odjavu
		JMenuItem odjavaMenuItem = new JMenuItem("Odjava");
		odjavaMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Akcija za odjavu
				JOptionPane.showMessageDialog(prodekanFrame, "Odjavljeni ste!", "Info",
						JOptionPane.INFORMATION_MESSAGE);
				prodekanFrame.dispose(); // Zatvori prozor prodekana
				new Login2(); // Prikaz prozora prijave
			}
		});
		menuBar.add(odjavaMenuItem);
		prodekanFrame.setJMenuBar(menuBar);

		// Panel za unos i ažuriranje zvanja
		JPanel zvanjePanel = new JPanel(new BorderLayout());
		JLabel nastavnikLabel = new JLabel("Odaberite nastavnika:");
		DefaultListModel<Nastavnik> nastavniciListModel = new DefaultListModel<>();
		List<Nastavnik> nastavnici = prodekanService.getNastavnici();
		for (Nastavnik nastavnik : nastavnici) {
			nastavniciListModel.addElement(nastavnik);
		}

		JList<Nastavnik> nastavniciList = new JList<>(nastavniciListModel);
		JScrollPane nastavniciScrollPane = new JScrollPane(nastavniciList);

		JTextField searchField = new JTextField(20);
		PromptSupport.setPrompt("Pretrazujte...", searchField);
		PromptSupport.setFocusBehavior(FocusBehavior.SHOW_PROMPT, searchField);

		searchField.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				updateList();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				updateList();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				updateList();
			}

			private void updateList() {
				String searchText = searchField.getText().toLowerCase();
				DefaultListModel<Nastavnik> filteredModel = new DefaultListModel<>();

				for (int i = 0; i < nastavniciListModel.size(); i++) {
					Nastavnik item = nastavniciListModel.getElementAt(i);
					if (item.toString().toLowerCase().contains(searchText)) {
						filteredModel.addElement(item);
					}
				}

				nastavniciList.setModel(filteredModel);
			}
		});

		zvanjePanel.add(nastavnikLabel, BorderLayout.WEST);
		zvanjePanel.add(searchField, BorderLayout.NORTH);
		zvanjePanel.add(nastavniciScrollPane, BorderLayout.CENTER);

		JPanel zvanjeButtonPanel = new JPanel(new BorderLayout());
		//JTextField zvanjeField = new JTextField(30);
		DefaultListModel<String> zvanjeModel=new DefaultListModel<>();
		zvanjeModel.addElement(new String("vanredan profesor"));
		zvanjeModel.addElement(new String("redovan profesor"));
		zvanjeModel.addElement(new String("docent"));
		JList<String> zvanjeList=new JList<>(zvanjeModel);
		JScrollPane zvanjeScroll=new JScrollPane(zvanjeList);
		
		
		JButton azurirajButton = new JButton("Ažuriraj zvanje");
	
		zvanjeButtonPanel.add(zvanjeScroll,BorderLayout.CENTER);
		zvanjeButtonPanel.add(azurirajButton,BorderLayout.SOUTH);
		zvanjePanel.add(zvanjeButtonPanel, BorderLayout.SOUTH);

		// Panel za unos i ažuriranje predmeta
		JPanel predmetPanel = new JPanel(new BorderLayout());
		JLabel predmetLabel = new JLabel("Odaberite predmet:");
		DefaultListModel<Predmet> predmetListModel = new DefaultListModel<>();
		List<Predmet> predmeti = prodekanService.getPredmeti();

		for (Predmet predmet : predmeti) {
			predmetListModel.addElement(predmet);
		}

		JList<Predmet> predmetList = new JList<>(predmetListModel);
		JScrollPane predmetScrollPane = new JScrollPane(predmetList);
		predmetPanel.add(predmetLabel, BorderLayout.NORTH);
		predmetPanel.add(predmetScrollPane, BorderLayout.CENTER);

		JPanel predmetButtonPanel = new JPanel();
		// JTextField zvanjeField = new JTextField(30);
		JButton azurirajPredmet = new JButton("Dodaj nastavnika");
		azurirajPredmet.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("1");
				Nastavnik selectedNastavnik = nastavniciList.getSelectedValue();
				System.out.println("1");

				System.out.println(selectedNastavnik);
				Predmet selectedPredmet = predmetList.getSelectedValue();
				System.out.println(selectedPredmet);
				Predmet azurirani = prodekanService.professorTosubject(selectedNastavnik, selectedPredmet);
				predmetListModel.setElementAt(azurirani, predmetList.getSelectedIndex());
			}
		});
		
		JPanel predmetButtonPanel2=new JPanel(new GridLayout(1,3));
		JTextField predusloviTextField=new JTextField();
		JButton azurirajPredusloveButton=new JButton("Azuriraj preduslove");
		predmetButtonPanel2.add(predusloviTextField);
		predmetButtonPanel2.add(azurirajPredusloveButton);
		predmetButtonPanel2.add(azurirajPredmet);
		
		azurirajPredusloveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent a) {
				Predmet p=predmetList.getSelectedValue();
				String novipreduslovi=predusloviTextField.getText();
				if(p!=null&&novipreduslovi!=null) {
					Predmet p2=prodekanService.promjeniPreduslove(p,novipreduslovi);
					predmetListModel.setElementAt(p2, predmetList.getSelectedIndex());
				}else {
					JOptionPane.showMessageDialog(prodekanFrame,
							"Oznacite predmet i unesite preduslove",
							"Greška", JOptionPane.ERROR_MESSAGE);
					
				}
				
			}
		});
		
		azurirajButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Nastavnik selectedNastavnik = nastavniciList.getSelectedValue();
				if (selectedNastavnik != null) {
					String novoZvanje = zvanjeList.getSelectedValue();
					System.out.println(novoZvanje);
					Nastavnik azuriraniNastavnik = prodekanService.azurirajZvanje(selectedNastavnik, novoZvanje);

					nastavniciListModel.setElementAt(azuriraniNastavnik, nastavniciList.getSelectedIndex());
					predmetListModel.clear();
					predmetListModel.addAll(prodekanService.getPredmeti());
					
					JOptionPane.showMessageDialog(prodekanFrame, "Zvanje ažurirano!", "Info",
							JOptionPane.INFORMATION_MESSAGE);
				} else {
					JOptionPane.showMessageDialog(prodekanFrame, "Odaberite nastavnika iz liste!", "Greška",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		// predmetButtonPanel.add(zvanjeField);
		predmetButtonPanel.add(predmetButtonPanel2);
		predmetPanel.add(predmetButtonPanel, BorderLayout.SOUTH);

		// JSplitPane
		JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, zvanjePanel, predmetPanel);
		splitPane.setResizeWeight(0.5); // Postavljamo početnu proporciju veličine lista, možete prilagoditi po želji

		JPanel akademskeGodinePanel = new JPanel(new GridLayout(1, 3));

		// Panel za akademske godine i dodavanje nove godine
		JPanel akademskaGodinaPanel = new JPanel(new BorderLayout());

		DefaultListModel<AkademskaGodina> akademskaGodinaModel = new DefaultListModel<>();
		for (AkademskaGodina ak : prodekanService.findAcademicYears())
			akademskaGodinaModel.addElement(ak);
		JList<AkademskaGodina> akademskaGodinaList = new JList<>(akademskaGodinaModel);
		JScrollPane akademskaGodinaScrollPane = new JScrollPane(akademskaGodinaList);
		akademskaGodinaPanel.add(akademskaGodinaScrollPane, BorderLayout.CENTER);

		JPanel dodavanjeGodinePanel = new JPanel(new GridLayout(7, 2));

		JLabel godinaLabel = new JLabel("akademska godina:");
		JTextField godinaField = new JTextField();

		JLabel datumPocetkaLabel = new JLabel("Pocetak:");
		JFormattedTextField datumPocetkaField = new JFormattedTextField(new SimpleDateFormat("dd.MM.yyyy HH:mm"));

		JLabel datumKrajaLabel = new JLabel("Kraj:");
		JFormattedTextField datumKrajaField = new JFormattedTextField(new SimpleDateFormat("dd.MM.yyyy HH:mm"));

		JLabel datumPocetkaUpisaLabel = new JLabel("Pocetak upisa");
		JFormattedTextField datumPocetkaUpisaField = new JFormattedTextField(new SimpleDateFormat("dd.MM.yyyy HH:mm"));

		JLabel datumKrajaUpisaLabel = new JLabel("Kraj upisa");
		JFormattedTextField datumKrajaUpisaField = new JFormattedTextField(new SimpleDateFormat("dd.MM.yyyy HH:mm"));

		JButton dodajGodinuButton = new JButton("Dodaj Godinu");
		dodajGodinuButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");
				SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");
				java.util.Date parsedDate1 = null;
				java.util.Date parsedDate2 = null;
				java.util.Date parsedDateTime1 = null;
				java.util.Date parsedDateTime2 = null;

				if (godinaField.getText().isEmpty() || datumPocetkaField.getText().isEmpty()
						|| datumKrajaField.getText().isEmpty() || datumPocetkaUpisaField.getText().isEmpty()
						|| datumKrajaUpisaField.getText().isEmpty()) {
					JOptionPane.showMessageDialog(null, "Niste unijeli sva polja");
					return;
				}

				try {
					parsedDate1 = dateFormat.parse(datumPocetkaField.getText());
					parsedDate2 = dateFormat.parse(datumKrajaField.getText());
					parsedDateTime1 = dateTimeFormat.parse(datumPocetkaUpisaField.getText());
					parsedDateTime2 = dateTimeFormat.parse(datumKrajaUpisaField.getText());

				} catch (ParseException ex) {
					// Obrada greške u slučaju da unos nije valjan
					ex.printStackTrace();
				}
				String pattern = "\\d{4}/\\d{4}";

				if (!Pattern.matches(pattern, godinaField.getText())) {
					JOptionPane.showMessageDialog(null, "Neispravan format akademske godine");
					return;
				}

				AkademskaGodina ak = new AkademskaGodina();

				ak.setGodina(godinaField.getText());
				ak.setDatumPocetka(new java.sql.Timestamp(parsedDate1.getTime()));
				ak.setDatumKraja(new java.sql.Timestamp(parsedDate2.getTime()));
				ak.setDatumPocetkaUpisa(new java.sql.Timestamp(parsedDateTime1.getTime()));
				ak.setDatumKrajaUpisa(new java.sql.Timestamp(parsedDateTime2.getTime()));

				if (prodekanService.dodajAkademskuGodinu(ak)) {
					akademskaGodinaModel.addElement(ak);
				} else {
					JOptionPane.showMessageDialog(prodekanFrame,
							"Nije moguce kreirati akademsku godinu ako svaki " + "predmet nema svog nastavnika",
							"Greška", JOptionPane.ERROR_MESSAGE);
				}
//				prodekanService.dodajAkademskuGodinu(ak);
//				akademskaGodinaModel.addElement(ak);

			}

		});
		// Dodajte komponente na panel
		dodavanjeGodinePanel.add(godinaLabel);
		dodavanjeGodinePanel.add(godinaField);
		dodavanjeGodinePanel.add(datumPocetkaLabel);
		dodavanjeGodinePanel.add(datumPocetkaField);
		dodavanjeGodinePanel.add(datumKrajaLabel);
		dodavanjeGodinePanel.add(datumKrajaField);
		dodavanjeGodinePanel.add(datumPocetkaUpisaLabel);
		dodavanjeGodinePanel.add(datumPocetkaUpisaField);
		dodavanjeGodinePanel.add(datumKrajaUpisaLabel);
		dodavanjeGodinePanel.add(datumKrajaUpisaField);
		dodavanjeGodinePanel.add(new JLabel()); // Prazna komponenta za poravnanje
		dodavanjeGodinePanel.add(dodajGodinuButton); // Dodajte gumb za dodavanje

		akademskaGodinaPanel.add(dodavanjeGodinePanel, BorderLayout.SOUTH);

		akademskeGodinePanel.add(akademskaGodinaPanel);

		// Panel za akademski predmeti i pregled
		JPanel akademskiPredmetPanel = new JPanel(new BorderLayout());

		DefaultListModel<AkademskiPredmet> akademskiPredmetModel = new DefaultListModel<>();

		JList<AkademskiPredmet> akademskiPredmetList = new JList<>(akademskiPredmetModel);
		JScrollPane akademskiPredmetScrollPane = new JScrollPane(akademskiPredmetList);
		akademskiPredmetPanel.add(akademskiPredmetScrollPane, BorderLayout.CENTER);

		JButton pregledajPredmeteButton = new JButton("Pregledaj  predmete");
		pregledajPredmeteButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				AkademskaGodina ak = akademskaGodinaList.getSelectedValue();
				akademskiPredmetModel.clear();
				akademskiPredmetModel.addAll(ak.getAkademskiPredmeti());

			}
		});
		// ...

		akademskiPredmetPanel.add(pregledajPredmeteButton, BorderLayout.SOUTH);

		akademskeGodinePanel.add(akademskiPredmetPanel);

		// Panel za studente i pregled
		JPanel studentPanel = new JPanel(new BorderLayout());

		DefaultListModel<Student> studentModel = new DefaultListModel<>();

		JList<Student> studentList = new JList<>(studentModel);
		JScrollPane studentScrollPane = new JScrollPane(studentList);
		studentPanel.add(studentScrollPane, BorderLayout.CENTER);

		JButton pregledajStudenteButton = new JButton("Pregledaj studente");
		// ...
		pregledajStudenteButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				AkademskiPredmet p = akademskiPredmetList.getSelectedValue();
				studentModel.clear();
				studentModel.addAll(p.getStudenti());
				System.out.println(p.getStudenti());
				System.out.println(p.getStudenti().size());

			}

		});
		studentPanel.add(pregledajStudenteButton, BorderLayout.SOUTH);

		akademskeGodinePanel.add(studentPanel);

		JPanel zahtjeviPanel = new JPanel(new BorderLayout());

		DefaultListModel<Zahtjev> zahtjeviModel = new DefaultListModel<>();
		JList<Zahtjev> zahtjeviList = new JList<>(zahtjeviModel);
		JScrollPane zahtjeviScrollPane = new JScrollPane(zahtjeviList);
		zahtjeviPanel.add(zahtjeviScrollPane, BorderLayout.CENTER);

		JPanel  buttonPanel2=new JPanel(new GridLayout(1,4));
		
		JButton pregledajZahtjeveButton=new JButton("Pregledaj zahtjeve");
		pregledajZahtjeveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				AkademskaGodina nova=prodekanService.getNova();
				if(nova!=null) {
				zahtjeviModel.clear();
				zahtjeviModel.addAll(nova.getZahtjeviZaPromjenu());
				}else {
					JOptionPane.showMessageDialog(prodekanFrame, "ne postoji nova akademska godina", "Info",
							JOptionPane.INFORMATION_MESSAGE);
				}
				
			}
		});
		
		JButton obradiZahtjevButton = new JButton("Obradi zahtjev");
		obradiZahtjevButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Zahtjev z=zahtjeviList.getSelectedValue();
				
				if(z!=null&& !z.getStatus().equals("Na cekanju")) {
				Zahtjev z2=prodekanService.obradiZahtjev(z);
				zahtjeviModel.setElementAt(z2, zahtjeviList.getSelectedIndex());	
				}
			}
		});
		
		
		JButton zahtjevDetalji=new JButton("Detalji"); 
		zahtjevDetalji.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Zahtjev z= zahtjeviList.getSelectedValue();
				if(z!=null) {
					String detalji = "Detalji iz zahtjeva:\n"
		                    + "Student: " + z.getStudent().toString() + "\n"
		                    + "Izbacuje:"  + z.getTrenutni().toString() + "\n"
		                     + "Ubacuje:"  + z.getZamjenski().toString() + "\n"
		                    + "Obrazloženje: " + z.getObrazlozenje();

		            JOptionPane.showMessageDialog(
		                    null,
		                    detalji,
		                    "Detalji iz zahtjeva",
		                    JOptionPane.INFORMATION_MESSAGE
		            );
					
				}
			}
		});
		
		JButton izbrisiZahtjev=new JButton("Izbrisi");
		izbrisiZahtjev.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Zahtjev z= zahtjeviList.getSelectedValue();
				AkademskaGodina ak=prodekanService.getNova();
				if(z!=null&&ak!=null) {
					zahtjeviModel.remove(zahtjeviList.getSelectedIndex());
					prodekanService.izbrisiZahtjevZaPromjenu(z,ak);
					
				}
			}
		});
		
		
		buttonPanel2.add(pregledajZahtjeveButton);
		buttonPanel2.add(obradiZahtjevButton);
		buttonPanel2.add(izbrisiZahtjev);
		buttonPanel2.add(zahtjevDetalji);		JPanel buttonPanel = new JPanel(); // Panel za smještaj gumba
		buttonPanel.add(buttonPanel2); // Dodaj gumb u panel

		zahtjeviPanel.add(buttonPanel, BorderLayout.SOUTH); // Dodaj panel s gumbom na dno

		// Postavi veličinu panela na željenu veličinu
		zahtjeviPanel.setPreferredSize(new Dimension(400, 300)); // Primjer dimenzija

		// Postavi layout za buttonPanel tako da gumb bude centriran unutar panela
		buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

		// Dodaj zahtjeviPanel u tvoj glavni container/frame

		// TabbedPane
		JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.addTab("Ažuriranje", splitPane);
		tabbedPane.addTab("Akademske Godine", akademskeGodinePanel);
		tabbedPane.add("Zahtjevi", zahtjeviPanel);
		prodekanFrame.add(tabbedPane);
		prodekanFrame.setVisible(true);
	}

	private void openAdministratorGUI() {
		JFrame administratorFrame = new JFrame("Administrator GUI");
		administratorFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		administratorFrame.setSize(500, 300);

		// Gornja traka s izbornicima
		JMenuBar menuBar = new JMenuBar();

		JMenuItem odjavaMenuItem = new JMenuItem("Odjava"); // Promijenjen JMenu u JMenuItem
		menuBar.add(odjavaMenuItem);

		// Dodavanje akcija za izbornike

		// ...

		odjavaMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Akcija za odjavu
				JOptionPane.showMessageDialog(administratorFrame, "Odjavljeni ste!", "Info",
						JOptionPane.INFORMATION_MESSAGE);
				administratorFrame.dispose(); // Zatvori prozor administratora
				Login2 login = new Login2(); // Stvaranje nove instance klase Login
				login.setVisible(true); // Prikaz prozora prijave
			}
		});

		JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.addTab("Unos nastavnika", createNastavnikPanel());
		tabbedPane.addTab("Unos studenta", createStudentPanel());
		tabbedPane.addTab("Unos predmeta", createPredmetPanel());
		tabbedPane.addTab("Pregled", pregledPanel());

		administratorFrame.getContentPane().add(tabbedPane);

		// ...

		administratorFrame.setJMenuBar(menuBar);

		administratorFrame.setVisible(true);
	}

	private JPanel createNastavnikPanel() {
		JPanel panel = new JPanel(new GridLayout(9, 2)); // Promijenjen layout panela na GridLayout s 8 redova i 2
															// stupca

		// Komponente za unos podataka o nastavniku
		JLabel imeLabel = new JLabel("Ime:");
		JTextField imeField = new JTextField();

		JLabel prezimeLabel = new JLabel("Prezime:");
		JTextField prezimeField = new JTextField();
		

		JLabel zvanjeLabel = new JLabel("Zvanje:");
		DefaultListModel<String> zvanjeModel=new DefaultListModel<>();
		zvanjeModel.addElement(new String("vanredan profesor"));
		zvanjeModel.addElement(new String("redovan profesor"));
		zvanjeModel.addElement(new String("docent"));
		JList<String> zvanjeList=new JList<>(zvanjeModel);
		JScrollPane zvanjeScroll=new JScrollPane(zvanjeList);
		

		JLabel usernameLabel = new JLabel("Korisničko ime:");
		JTextField usernameField = new JTextField();

		JLabel passwordLabel = new JLabel("Lozinka:");
		JPasswordField passwordField = new JPasswordField();

		JLabel prodekanLabel = new JLabel("Prodekan:");
		JCheckBox prodekanCheckBox = new JCheckBox();

		JLabel studijskiProgramLabel = new JLabel("Odaberite studijski program:");
		DefaultListModel<StudijskiProgram> studijskiProgramiModel = new DefaultListModel<>();
		AdministratorService as = new AdministratorService();

		List<StudijskiProgram> studijskiProgrami = as.getStudijskiProgrami();

		for (StudijskiProgram sp : studijskiProgrami) {
			studijskiProgramiModel.addElement(sp);
		}

		JList<StudijskiProgram> studijskiProgramiList = new JList<>(studijskiProgramiModel);
		JScrollPane studijskiProgramiScrollPane = new JScrollPane(studijskiProgramiList);

		JLabel usmjerenjeLabel = new JLabel("Odaberite usmjerenje: ");
		DefaultListModel<Usmjerenje> usmjerenjeModel = new DefaultListModel<>();

		List<Usmjerenje> usmjerenja = as.getUsmjerenja();
		for (Usmjerenje u : usmjerenja) {
			usmjerenjeModel.addElement(u);
		}
		JList<Usmjerenje> usmjerenjeList = new JList<>(usmjerenjeModel);
		JScrollPane usmjrenjeScrollPane = new JScrollPane(usmjerenjeList);
		JButton spremiButton = new JButton("Spremi");
		spremiButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String ime = imeField.getText();
				String prezime = prezimeField.getText();
				String zvanje = zvanjeList.getSelectedValue();
				String username = usernameField.getText();
				String password = new String(passwordField.getPassword());
				boolean prodekan = prodekanCheckBox.isSelected();
				if (prodekan) {
					if (as.checkProdekanS()) {
						JOptionPane.showMessageDialog(panel, "Prodekan vec postoji.");
						return;
					}
				}
				if (ime.isEmpty() || prezime.isEmpty() || zvanje.isEmpty() || username.isEmpty()
						|| password.isEmpty()) {
					JOptionPane.showMessageDialog(panel, "Niste unijeli sva polja.");
					return;
				}

				for (char c : ime.toCharArray()) {
					// Provjeravamo da li je karakter broj
					if (Character.isDigit(c)) {
						JOptionPane.showMessageDialog(panel, "Neispravan unos imena");
						return;
					}
				}

				for (char c : prezime.toCharArray()) {
					// Provjeravamo da li je karakter broj
					if (Character.isDigit(c)) {
						JOptionPane.showMessageDialog(panel, "Neispravan unos prezimena");
						return;
					}
				}

				for (char c : zvanje.toCharArray()) {
					// Provjeravamo da li je karakter broj
					if (Character.isDigit(c)) {
						JOptionPane.showMessageDialog(panel, "Neispravan unos zvanja");
						return;
					}
				}

				Nastavnik n = new Nastavnik();
				n.setIme(ime);
				n.setPrezime(prezime);
				n.setUsername(username);
				n.setPassword(password);
				n.setZvanje(zvanje);
				n.setProdekan(prodekan);
				StudijskiProgram sp = studijskiProgramiList.getSelectedValue();
				Usmjerenje usmjerenje = usmjerenjeList.getSelectedValue();
				if (as.addProfessor(n, sp, usmjerenje)) {

					JOptionPane.showMessageDialog(panel, "Spremljeno!");
				} else {
					JOptionPane.showMessageDialog(panel, "Profesor vec postoji.");
				}
			}
		});

		// Dodavanje komponenti u panel
		panel.add(imeLabel);
		panel.add(imeField);
		panel.add(prezimeLabel);
		panel.add(prezimeField);
		panel.add(zvanjeLabel);
		panel.add(zvanjeScroll);
		panel.add(usernameLabel);
		panel.add(usernameField);
		panel.add(passwordLabel);
		panel.add(passwordField);
		panel.add(prodekanLabel);
		panel.add(prodekanCheckBox);
		panel.add(studijskiProgramLabel);
		panel.add(studijskiProgramiScrollPane);
		panel.add(usmjerenjeLabel);
		panel.add(usmjrenjeScrollPane);
		panel.add(spremiButton);

		return panel;
	}

	private JPanel createStudentPanel() {
		JPanel panel = new JPanel(new GridLayout(8, 2));

		// Komponente za unos podataka o studentu
		JLabel imeLabel = new JLabel("Ime:");
		JTextField imeField = new JTextField();

		JLabel prezimeLabel = new JLabel("Prezime:");
		JTextField prezimeField = new JTextField();

		JLabel brojIndeksaLabel = new JLabel("Broj indeksa:");
		JTextField brojIndeksaField = new JTextField();

		JLabel usernameLabel = new JLabel("Korisničko ime:");
		JTextField usernameField = new JTextField();

		JLabel passwordLabel = new JLabel("Lozinka:");
		JPasswordField passwordField = new JPasswordField();

		JLabel studijskiProgramLabel = new JLabel("Odaberite studijski program:");
		DefaultListModel<StudijskiProgram> studijskiProgramiModel = new DefaultListModel<>();
		AdministratorService as = new AdministratorService();

		List<StudijskiProgram> studijskiProgrami = as.getStudijskiProgrami();

		for (StudijskiProgram sp : studijskiProgrami) {
			studijskiProgramiModel.addElement(sp);
		}

		JList<StudijskiProgram> studijskiProgramiList = new JList<>(studijskiProgramiModel);
		JScrollPane studijskiProgramiScrollPane = new JScrollPane(studijskiProgramiList);

		JLabel usmjerenjeLabel = new JLabel("Odaberite usmjerenje: ");
		DefaultListModel<Usmjerenje> usmjerenjeModel = new DefaultListModel<>();

		List<Usmjerenje> usmjerenja = as.getUsmjerenja();
		for (Usmjerenje u : usmjerenja) {
			usmjerenjeModel.addElement(u);
		}
		JList<Usmjerenje> usmjerenjeList = new JList<>(usmjerenjeModel);
		JScrollPane usmjrenjeScrollPane = new JScrollPane(usmjerenjeList);

		JButton spremiButton = new JButton("Spremi");
		spremiButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Akcija za spremanje podataka o studentu
				String ime = imeField.getText();
				String prezime = prezimeField.getText();
				String brojIndeksa = brojIndeksaField.getText();
				String username = usernameField.getText();
				String password = new String(passwordField.getPassword());

				if (ime.isEmpty() || prezime.isEmpty() || brojIndeksa.isEmpty() || username.isEmpty()
						|| password.isEmpty()) {
					JOptionPane.showMessageDialog(panel, "Niste unijeli sva polja");
					return;
				}

				for (char c : ime.toCharArray()) {
					// Provjeravamo da li je karakter broj
					if (Character.isDigit(c)) {
						JOptionPane.showMessageDialog(panel, "Neispravan unos imena");
						return;
					}
				}

				for (char c : prezime.toCharArray()) {
					// Provjeravamo da li je karakter broj
					if (Character.isDigit(c)) {
						JOptionPane.showMessageDialog(panel, "Neispravan unos prezimena");
						return;
					}
				}

				Student s = new Student();
				s.setIme(ime);
				s.setPrezime(prezime);
				s.setBroj_indeksa(brojIndeksa);
				s.setUsername(username);
				s.setPassword(password);

				if (as.addStudent(s, studijskiProgramiList.getSelectedValue(), usmjerenjeList.getSelectedValue())) {
					JOptionPane.showMessageDialog(panel, "Spremljeno!");
				} else {
					JOptionPane.showMessageDialog(panel, "Student vec postoji.");
				}

				// Ovdje možete obraditi spremanje podataka, npr. pozvati odgovarajuću metodu za
				// spremanje u bazu podataka

				// Ili samo prikazati unesene podatke za provjeru
			}
		});

		// Dodavanje komponenti u panel
		panel.add(imeLabel);
		panel.add(imeField);
		panel.add(prezimeLabel);
		panel.add(prezimeField);
		panel.add(brojIndeksaLabel);
		panel.add(brojIndeksaField);
		panel.add(usernameLabel);
		panel.add(usernameField);
		panel.add(passwordLabel);
		panel.add(passwordField);

		// panel.add(new JLabel()); // Prazan prostor
		panel.add(studijskiProgramLabel);
		panel.add(studijskiProgramiScrollPane);
		panel.add(usmjerenjeLabel);
		panel.add(usmjrenjeScrollPane);
		panel.add(spremiButton);

		return panel;
	}

	private JPanel createPredmetPanel() {
		JPanel panel = new JPanel(new GridLayout(8, 2)); // Promijenjen layout panela na GridLayout s 7 redova i 2
															// stupca

		// Komponente za unos podataka o predmetu
		JLabel sifraLabel = new JLabel("Šifra predmeta:");
		JTextField sifraField = new JTextField();

		JLabel nazivLabel = new JLabel("Naziv predmeta:");
		JTextField nazivField = new JTextField();

		JLabel ectsLabel = new JLabel("Broj ECTS kredita:");
		JTextField ectsField = new JTextField();
		ectsField.setText("0");

		JLabel predusloviLabel = new JLabel("Preduslovi: ");
		  DefaultListModel<String> predusloviModel = new DefaultListModel<>();
		  predusloviModel.addElement("MAT1");
		  predusloviModel.addElement("MAT2");
		  predusloviModel.addElement("FIZ1");
		  predusloviModel.addElement("FIZ2");
		 
		  
	        JList<String> predusloviList = new JList<>(predusloviModel);
	        predusloviList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
	        JScrollPane predusloviScroll=new JScrollPane(predusloviList);
	        

		JLabel studijskiProgramLabel = new JLabel("Odaberite studijski program:");
		DefaultListModel<StudijskiProgram> studijskiProgramiModel = new DefaultListModel<>();
		AdministratorService as = new AdministratorService();

		List<StudijskiProgram> studijskiProgrami = as.getStudijskiProgrami();

		for (StudijskiProgram sp : studijskiProgrami) {
			studijskiProgramiModel.addElement(sp);
		}

		JList<StudijskiProgram> studijskiProgramiList = new JList<>(studijskiProgramiModel);
		JScrollPane studijskiProgramiScrollPane = new JScrollPane(studijskiProgramiList);

		JLabel usmjerenjeLabel = new JLabel("Odaberite usmjerenje: ");
		DefaultListModel<Usmjerenje> usmjerenjeModel = new DefaultListModel<>();

		List<Usmjerenje> usmjerenja = as.getUsmjerenja();
		for (Usmjerenje u : usmjerenja) {
			usmjerenjeModel.addElement(u);
		}
		JList<Usmjerenje> usmjerenjeList = new JList<>(usmjerenjeModel);
		JScrollPane usmjrenjeScrollPane = new JScrollPane(usmjerenjeList);

		JLabel semestarLabel = new JLabel("Odaberite semestar ");

		DefaultListModel<Semestar> semestarModel = new DefaultListModel<>();
		List<Semestar> semestri = as.getSemestri();
		semestarModel.addAll(semestri);
		JList<Semestar> semestarList = new JList<>(semestarModel);
		JScrollPane semestarScrollPane = new JScrollPane(semestarList);

		JButton spremiButton = new JButton("Spremi");
		spremiButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Akcija za spremanje podataka o predmetu
//				String sifra = sifraField.getText();
//				String naziv = nazivField.getText();
//				int ects = Integer.parseInt(ectsField.getText());
//				String predusloviString = predusloviField.getText();
//				String[] preduslovi = predusloviString.split(",");
//				System.out.println(preduslovi[0]);
//				List<String> predusloviList = new ArrayList<>(Arrays.asList(preduslovi));
//
//				if (sifra.isEmpty() || naziv.isEmpty()) {
//					JOptionPane.showMessageDialog(null, "Niste unijeli sva polja.");
//					return;
//				}
				String sifra = sifraField.getText();
				String naziv = nazivField.getText();

			//	String predusloviString = predusloviField.getText();
			//	String[] preduslovi = predusloviString.split(",");
				//System.out.println(preduslovi[0]);
				//List<String> predusloviList = new ArrayList<>(Arrays.asList(preduslovi));

				if (sifra.isEmpty() || naziv.isEmpty()) {
					JOptionPane.showMessageDialog(null, "Niste unijeli sva polja.");
					return;
				}

				try {
					int ects = Integer.parseInt(ectsField.getText());
				} catch (NumberFormatException ex) {
					JOptionPane.showMessageDialog(null, "Neispravan unos ECTS kredita.");
					return;
				}
				int ects = Integer.parseInt(ectsField.getText());

				List<String> preduslovi=predusloviList.getSelectedValuesList();
				String a=new String();
				
				for(String b:preduslovi) {
					a+=b+" ";
				}
				
				System.out.println(a);
				
				Predmet p = new Predmet();
				p.setSifra_predmeta(sifra);
				p.setNaziv(naziv);
				p.setEcts(ects);
				//p.setSemestar(semestar);
				p.setPreduslovi(a);
				StudijskiProgram sp = studijskiProgramiList.getSelectedValue();
				Usmjerenje u = usmjerenjeList.getSelectedValue();
				Semestar s = semestarList.getSelectedValue();

				if (as.addPredmet(sp, p, u, s)) {

					// Postavi odabrani studijski program u predmet

					JOptionPane.showMessageDialog(panel, "Spremljeno!");
				} else {

					JOptionPane.showMessageDialog(panel, "Predmet vec postoji.");
				}

				// Ovdje možete obraditi spremanje podataka, npr. pozvati odgovarajuću metodu za
				// spremanje u bazu podataka

				// Ili samo prikazati unesene podatke za provjeru

			}
		});

		// Dodavanje komponenti u panel
		panel.add(sifraLabel);
		panel.add(sifraField);
		panel.add(nazivLabel);
		panel.add(nazivField);
		panel.add(ectsLabel);
		panel.add(ectsField);
		panel.add(studijskiProgramLabel);
		panel.add(studijskiProgramiScrollPane);
		panel.add(usmjerenjeLabel);
		panel.add(usmjrenjeScrollPane);
		panel.add(semestarLabel);
		panel.add(semestarScrollPane);
		panel.add(predusloviLabel);
		panel.add(predusloviScroll);

		panel.add(spremiButton);

		return panel;
	}
	
	private JPanel pregledPanel() {
		AdministratorService admin=new AdministratorService();
		JPanel panel = new JPanel(new GridLayout(3, 1));
		
		JPanel predmetPanel = new JPanel(new BorderLayout());

		DefaultListModel<Predmet> predmetModel = new DefaultListModel<>();
		
		JList<Predmet> predmetList = new JList<>(predmetModel);
		JScrollPane predmetScrollPane = new JScrollPane(predmetList);
         
		
		
		
		JButton pregledajPredmeteButton=new JButton("Pregledaj predmete");
		JButton promjeniNaziv=new JButton("Promjeni naziv");
		JButton promjeniSifru=new JButton("Promjeni sifru predmeta");
		
		
		JPanel promjenePanel2=new JPanel(new GridLayout(1,3));
		promjenePanel2.add(pregledajPredmeteButton);
	    promjenePanel2.add(promjeniSifru);
	    promjenePanel2.add(promjeniNaziv);
	    
	    promjeniSifru.addActionListener(new ActionListener() {
	        @Override
	        public void actionPerformed(ActionEvent e) {
	            Predmet p = predmetList.getSelectedValue();
	            if (p != null) {
	                String novaSifra = JOptionPane.showInputDialog(
	                        null,
	                        "Unesite novo:",
	                        "Promjena",
	                        JOptionPane.PLAIN_MESSAGE
	                );

	                if (novaSifra != null && !novaSifra.isEmpty()) {
	                    p.setSifra_predmeta(novaSifra);
	                   Predmet p2= admin.mergePredmet(p);
	                    predmetModel.setElementAt(p2, predmetList.getSelectedIndex());
	                    // Ažurirajte objekat u bazi ili gde god je potrebno
	                }
	            }
	        }
	    });
	    
	    
	    promjeniNaziv.addActionListener(new ActionListener() {
	    	 public void actionPerformed(ActionEvent e) {
		            Predmet p = predmetList.getSelectedValue();
		            if (p != null) {
		            	  String novaSifra = JOptionPane.showInputDialog(
			                        null,
			                        "Unesite novo:",
			                        "Promjena",
			                        JOptionPane.PLAIN_MESSAGE
			                );

		                if (novaSifra != null && !novaSifra.isEmpty()) {
		                    p.setNaziv(novaSifra);
		                   Predmet p2= admin.mergePredmet(p);
		                    predmetModel.setElementAt(p2, predmetList.getSelectedIndex());
		                    // Ažurirajte objekat u bazi ili gde god je potrebno
		                }
		            }
		        }
	    });

		
		
		pregledajPredmeteButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
			
				List<Predmet> predmeti=admin.getPredmeti();
				predmetModel.clear();
				predmetModel.addAll(predmeti);
				
			}
		});
		
		
		JTextField searchField = new JTextField(20);
		PromptSupport.setPrompt("Pretrazujte...", searchField);
		PromptSupport.setFocusBehavior(FocusBehavior.SHOW_PROMPT, searchField);

		searchField.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				updateList();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				updateList();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				updateList();
			}

			private void updateList() {
				String searchText = searchField.getText().toLowerCase();
				DefaultListModel<Predmet> filteredModel = new DefaultListModel<>();

				for (int i = 0; i < predmetModel.size(); i++) {
					 Predmet item = predmetModel.getElementAt(i);
					if (item.toString().toLowerCase().contains(searchText)) {
						filteredModel.addElement(item);
					}
				}

				predmetList.setModel(filteredModel);
			}
		});
        
		predmetPanel.add(searchField, BorderLayout.NORTH);
		predmetPanel.add(predmetScrollPane, BorderLayout.CENTER);
		predmetPanel.add(promjenePanel2,BorderLayout.SOUTH);
		
		
		
		JPanel studentPanel = new JPanel(new BorderLayout());

		DefaultListModel<Student> studentModel = new DefaultListModel<>();
		
		JList<Student> studentList = new JList<>(studentModel);
		JScrollPane studentScrollPane = new JScrollPane(studentList);

		
		
		
		JButton pregledajStudenteButton=new JButton("Pregledaj studente");
		
		JButton promjeniIme=new JButton("Promjeni ime");
		JButton promjeniPrezime=new JButton("Promjeni prezime");
		JButton promjeniUsername=new JButton("Promjeni Username");
		JButton promjeniPassword=new JButton("Promjeni lozinku");
		
		JPanel promjenePanel=new JPanel(new GridLayout(1,5));
		promjenePanel.add(pregledajStudenteButton);
		promjenePanel.add(promjeniIme);
		promjenePanel.add(promjeniPrezime);
		promjenePanel.add(promjeniUsername);
		promjenePanel.add(promjeniPassword);
		
		
		 promjeniIme.addActionListener(new ActionListener() {
	    	 public void actionPerformed(ActionEvent e) {
		            Student p = studentList.getSelectedValue();
		            if (p != null) {
		            	  String novaSifra = JOptionPane.showInputDialog(
			                        null,
			                        "Unesite novo:",
			                        "Promjena",
			                        JOptionPane.PLAIN_MESSAGE
			                );

		                if (novaSifra != null && !novaSifra.isEmpty()) {
		                    p.setIme(novaSifra);
		                   Student p2= admin.mergeStudent(p);
		                    studentModel.setElementAt(p2, studentList.getSelectedIndex());
		                    // Ažurirajte objekat u bazi ili gde god je potrebno
		                }
		            }
		        }
	    });
		 
		 
		 promjeniPrezime.addActionListener(new ActionListener() {
	    	 public void actionPerformed(ActionEvent e) {
		            Student p = studentList.getSelectedValue();
		            if (p != null) {
		            	  String novaSifra = JOptionPane.showInputDialog(
			                        null,
			                        "Unesite novo:",
			                        "Promjena",
			                        JOptionPane.PLAIN_MESSAGE
			                );

		                if (novaSifra != null && !novaSifra.isEmpty()) {
		                    p.setPrezime(novaSifra);
		                   Student p2= admin.mergeStudent(p);
		                    studentModel.setElementAt(p2, studentList.getSelectedIndex());
		                    // Ažurirajte objekat u bazi ili gde god je potrebno
		                }
		            }
		        }
	    });
		 
		 promjeniUsername.addActionListener(new ActionListener() {
	    	 public void actionPerformed(ActionEvent e) {
		            Student p = studentList.getSelectedValue();
		            if (p != null) {
		            	  String novaSifra = JOptionPane.showInputDialog(
			                        null,
			                        "Unesite novo:",
			                        "Promjena",
			                        JOptionPane.PLAIN_MESSAGE
			                );

		                if (novaSifra != null && !novaSifra.isEmpty()) {
		                    p.setUsername(novaSifra);
		                   Student p2= admin.mergeStudent(p);
		                    studentModel.setElementAt(p2, studentList.getSelectedIndex());
		                    // Ažurirajte objekat u bazi ili gde god je potrebno
		                }
		            }
		        }
	    });
		 
		 
		 promjeniPassword.addActionListener(new ActionListener() {
	    	 public void actionPerformed(ActionEvent e) {
		            Student p = studentList.getSelectedValue();
		            if (p != null) {
		            	  String novaSifra = JOptionPane.showInputDialog(
			                        null,
			                        "Unesite novo:",
			                        "Promjena",
			                        JOptionPane.PLAIN_MESSAGE
			                );

		                if (novaSifra != null && !novaSifra.isEmpty()) {
		                    p.setPassword(novaSifra);
		                   Student p2= admin.mergeStudent(p);
		                    studentModel.setElementAt(p2, studentList.getSelectedIndex());
		                    // Ažurirajte objekat u bazi ili gde god je potrebno
		                }
		            }
		        }
	    });
		
		pregledajStudenteButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
			
				List<Student> predmeti=admin.getStudenti();
				studentModel.clear();
				studentModel.addAll(predmeti);
				
			}
		});
		
		
		JTextField searchField2 = new JTextField(20);
		PromptSupport.setPrompt("Pretrazujte...", searchField2);
		PromptSupport.setFocusBehavior(FocusBehavior.SHOW_PROMPT, searchField2);

		searchField2.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				updateList();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				updateList();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				updateList();
			}

			private void updateList() {
				String searchText = searchField.getText().toLowerCase();
				DefaultListModel<Student> filteredModel = new DefaultListModel<>();

				for (int i = 0; i < studentModel.size(); i++) {
					 Student item = studentModel.getElementAt(i);
					if (item.toString().toLowerCase().contains(searchText)) {
						filteredModel.addElement(item);
					}
				}

				studentList.setModel(filteredModel);
			}
		});
		
		studentPanel.add(searchField2,BorderLayout.NORTH);
		studentPanel.add(promjenePanel,BorderLayout.SOUTH);
		studentPanel.add(studentScrollPane,BorderLayout.CENTER);
		

		JPanel nastavnikPanel = new JPanel(new BorderLayout());

		DefaultListModel<Nastavnik> nastavnikModel = new DefaultListModel<>();
		
		JList<Nastavnik>  nastavnikList = new JList<>(nastavnikModel);
		JScrollPane nastavniktScrollPane = new JScrollPane(nastavnikList);

		
		
		
		JButton pregledajStudenteButton3=new JButton("Pregledaj nastavnike");
		
		JButton promjeniIme3=new JButton("Promjeni ime");
		JButton promjeniPrezime3=new JButton("Promjeni prezime");
		JButton promjeniUsername3=new JButton("Promjeni Username");
		JButton promjeniPassword3=new JButton("Promjeni lozinku");
		JPanel promjenePanel3=new JPanel(new GridLayout(1,5));
		promjenePanel3.add(pregledajStudenteButton3);
		promjenePanel3.add(promjeniIme3);
		promjenePanel3.add(promjeniPrezime3);
		promjenePanel3.add(promjeniUsername3);
		promjenePanel3.add(promjeniPassword3);
		
		 promjeniIme3.addActionListener(new ActionListener() {
	    	 public void actionPerformed(ActionEvent e) {
		            Nastavnik p = nastavnikList.getSelectedValue();
		            if (p != null) {
		            	  String novaSifra = JOptionPane.showInputDialog(
			                        null,
			                        "Unesite novo:",
			                        "Promjena",
			                        JOptionPane.PLAIN_MESSAGE
			                );

		                if (novaSifra != null && !novaSifra.isEmpty()) {
		                    p.setIme(novaSifra);
		                   Nastavnik p2= admin.mergeNastavnik(p);
		                    nastavnikModel.setElementAt(p2, nastavnikList.getSelectedIndex());
		                    // Ažurirajte objekat u bazi ili gde god je potrebno
		                }
		            }
		        }
	    });
		 
		 promjeniPrezime3.addActionListener(new ActionListener() {
	    	 public void actionPerformed(ActionEvent e) {
		            Nastavnik p = nastavnikList.getSelectedValue();
		            if (p != null) {
		            	  String novaSifra = JOptionPane.showInputDialog(
			                        null,
			                        "Unesite novo:",
			                        "Promjena",
			                        JOptionPane.PLAIN_MESSAGE
			                );

		                if (novaSifra != null && !novaSifra.isEmpty()) {
		                    p.setPrezime(novaSifra);
		                   Nastavnik p2= admin.mergeNastavnik(p);
		                    nastavnikModel.setElementAt(p2, nastavnikList.getSelectedIndex());
		                    // Ažurirajte objekat u bazi ili gde god je potrebno
		                }
		            }
		        }
	    });
		 
		 
		 promjeniUsername3.addActionListener(new ActionListener() {
	    	 public void actionPerformed(ActionEvent e) {
		            Nastavnik p = nastavnikList.getSelectedValue();
		            if (p != null) {
		            	  String novaSifra = JOptionPane.showInputDialog(
			                        null,
			                        "Unesite novo:",
			                        "Promjena",
			                        JOptionPane.PLAIN_MESSAGE
			                );

		                if (novaSifra != null && !novaSifra.isEmpty()) {
		                    p.setUsername(novaSifra);
		                   Nastavnik p2= admin.mergeNastavnik(p);
		                    nastavnikModel.setElementAt(p2, nastavnikList.getSelectedIndex());
		                    // Ažurirajte objekat u bazi ili gde god je potrebno
		                }
		            }
		        }
	    });
		 
		 
		 promjeniPassword3.addActionListener(new ActionListener() {
	    	 public void actionPerformed(ActionEvent e) {
		            Nastavnik p = nastavnikList.getSelectedValue();
		            if (p != null) {
		            	  String novaSifra = JOptionPane.showInputDialog(
			                        null,
			                        "Unesite novo:",
			                        "Promjena",
			                        JOptionPane.PLAIN_MESSAGE
			                );

		                if (novaSifra != null && !novaSifra.isEmpty()) {
		                    p.setPassword(novaSifra);
		                   Nastavnik p2= admin.mergeNastavnik(p);
		                    nastavnikModel.setElementAt(p2, nastavnikList.getSelectedIndex());
		                    // Ažurirajte objekat u bazi ili gde god je potrebno
		                }
		            }
		        }
	    });
	
		
		pregledajStudenteButton3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
			
				List<Nastavnik> predmeti=admin.getNastavnici();
				nastavnikModel.clear();
				nastavnikModel.addAll(predmeti);
				
			}
		});
		
		
		JTextField searchField3 = new JTextField(20);
		PromptSupport.setPrompt("Pretrazujte...", searchField3);
		PromptSupport.setFocusBehavior(FocusBehavior.SHOW_PROMPT, searchField3);

		searchField3.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				updateList();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				updateList();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				updateList();
			}

			private void updateList() {
				String searchText = searchField3.getText().toLowerCase();
				DefaultListModel<Nastavnik> filteredModel = new DefaultListModel<>();

				for (int i = 0; i < nastavnikModel.size(); i++) {
					 Nastavnik item = nastavnikModel.getElementAt(i);
					if (item.toString().toLowerCase().contains(searchText)) {
						filteredModel.addElement(item);
					}
				}

				nastavnikList.setModel(filteredModel);
			}
		});
		
		
		nastavnikPanel.add(searchField3,BorderLayout.NORTH);
		nastavnikPanel.add(promjenePanel3,BorderLayout.SOUTH);
		nastavnikPanel.add(nastavniktScrollPane,BorderLayout.CENTER);
		
		
		panel.add(predmetPanel);
		panel.add(studentPanel);
		panel.add(nastavnikPanel);
		
		
		return panel;
	}

	public static void main(String[] args) {

		// run TestService optional (when DB empty)
//		TestService tst = new TestService();
	  //  tst.test();

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new Login2();
			}
		});
	}
}