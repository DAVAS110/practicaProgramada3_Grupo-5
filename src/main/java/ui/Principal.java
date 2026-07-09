package ui;

import exceptions.RegistroDuplicadoException;
import exceptions.RegistroNoEncontradoException;
import exceptions.ValidacionException;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;
import model.CursoEstudiante;
import model.EstadoMatricula;
import model.Estudiante;
import service.GestorMatriculas;

public class Principal extends JFrame {

    private final GestorMatriculas gestor;
    private DefaultTableModel modeloTabla;

    private JTable tblMatriculas;
    private JTextField txtCodigo;
    private JTextField txtNombre;
    private JTextField txtIdentificacion;
    private JTextField txtCreditos;
    private JComboBox<CursoEstudiante> cbCurso;
    private JComboBox<EstadoMatricula> cbEstado;

    private JButton btnRegistrar;
    private JButton btnEditar;
    private JButton btnBuscar;
    private JButton btnEliminar;
    private JButton btnLimpiar;
    private JButton btnActualizar;
    private JButton btnSalir;

    private JMenuItem mnuNuevo;
    private JMenuItem mnuSalir;
    private JMenuItem mnuRegistrar;
    private JMenuItem mnuEditar;
    private JMenuItem mnuEliminar;
    private JMenuItem mnuBuscar;
    private JMenuItem mnuAcerca;

    public Principal(GestorMatriculas gestor) {
        this.gestor = gestor;
        initComponents();
        setLocationRelativeTo(null);
        setTitle("Gestión de Matrícula - Grupo 5");
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent evt) {
                cerrar();
            }
        });
        configurarTabla();
        refrescarTabla();
    }

    private void configurarTabla() {
        modeloTabla = new DefaultTableModel(
                new Object[]{"Código", "Estudiante", "Curso", "Créditos", "Estado"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tblMatriculas.setModel(modeloTabla);
        tblMatriculas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblMatriculas.getSelectionModel().addListSelectionListener(evt -> {
            if (evt.getValueIsAdjusting()) {
                return;
            }
            int fila = tblMatriculas.getSelectedRow();
            if (fila < 0) {
                return;
            }
            String codigo = modeloTabla.getValueAt(fila, 0).toString();
            try {
                Estudiante e = gestor.buscar(codigo);
                cargarFormMatricula(e);
            } catch (ValidacionException | RegistroNoEncontradoException ex) {
                mostrarError(ex.getMessage());
            }
        });
    }

    private void refrescarTabla() {
        modeloTabla.setRowCount(0);

        List<Estudiante> lista = gestor.listar();

        for (Estudiante e : lista) {
            modeloTabla.addRow(new Object[]{
                e.getCodigoMatricula(),
                e.getNombreEstudiante(),
                e.getCurso(),
                e.getCreditos(),
                e.getEstado()
            });
        }
    }

    private void registrar() {
        activarAcciones(false);
        try {
            EstudianteDialog dialog = new EstudianteDialog(this, EstudianteDialog.Mode.CREAR, null);
            dialog.setVisible(true);

            Estudiante nuevo = dialog.getResult();
            if (nuevo == null) {
                return;
            }

            try {
                gestor.crear(nuevo);
                limpiar();
            } catch (ValidacionException | RegistroDuplicadoException ex) {
                mostrarError(ex.getMessage());
            } finally {
                refrescarTabla();
            }
        } finally {
            activarAcciones(true);
        }
    }

    private void editar() {
        String codigo = obtenerCodigo();
        if (codigo == null) {
            return;
        }

        activarAcciones(false);
        try {
            Estudiante existente;
            try {
                existente = gestor.buscar(codigo);
            } catch (ValidacionException | RegistroNoEncontradoException ex) {
                mostrarError(ex.getMessage());
                return;
            }

            EstudianteDialog dialog = new EstudianteDialog(this, EstudianteDialog.Mode.EDITAR, existente);
            dialog.setVisible(true);

            Estudiante editado = dialog.getResult();
            if (editado == null) {
                return;
            }

            try {
                gestor.editar(editado);
                seleccionarFilaPorCodigo(editado.getCodigoMatricula());
            } catch (ValidacionException | RegistroNoEncontradoException ex) {
                mostrarError(ex.getMessage());
            } finally {
                refrescarTabla();
            }
        } finally {
            activarAcciones(true);
        }
    }

    private void buscar() {
        activarAcciones(false);
        try {
            String codigo = txtCodigo.getText();

            try {
                Estudiante e = gestor.buscar(codigo);
                cargarFormMatricula(e);
                seleccionarFilaPorCodigo(e.getCodigoMatricula());
            } catch (ValidacionException | RegistroNoEncontradoException ex) {
                mostrarError(ex.getMessage());
            } finally {
                txtCodigo.requestFocusInWindow();
                txtCodigo.selectAll();
            }
        } finally {
            activarAcciones(true);
        }
    }

    private void eliminar() {
        String codigo = obtenerCodigo();
        if (codigo == null) {
            return;
        }

        int opt = JOptionPane.showConfirmDialog(
                this,
                "¿Desea eliminar la matrícula " + codigo + "?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION
        );
        if (opt != JOptionPane.YES_OPTION) {
            return;
        }

        activarAcciones(false);
        try {
            try {
                gestor.eliminar(codigo);
                limpiar();
            } catch (ValidacionException | RegistroNoEncontradoException ex) {
                mostrarError(ex.getMessage());
            } finally {
                refrescarTabla();
            }
        } finally {
            activarAcciones(true);
        }
    }

    private void cerrar() {
        int opt = JOptionPane.showConfirmDialog(
                this,
                "¿Desea salir del sistema?",
                "Salir",
                JOptionPane.YES_NO_OPTION
        );
        if (opt == JOptionPane.YES_OPTION) {
            dispose();
            System.exit(0);
        }
    }

    private void limpiar() {
        txtCodigo.setText("");
        txtNombre.setText("");
        txtIdentificacion.setText("");
        txtCreditos.setText("");
        cbCurso.setSelectedIndex(0);
        cbEstado.setSelectedIndex(0);
        tblMatriculas.clearSelection();
        txtCodigo.requestFocusInWindow();
    }

    private void cargarFormMatricula(Estudiante e) {
        txtCodigo.setText(e.getCodigoMatricula());
        txtNombre.setText(e.getNombreEstudiante());
        txtIdentificacion.setText(e.getIdentificacion());
        cbCurso.setSelectedItem(e.getCurso());
        txtCreditos.setText(String.valueOf(e.getCreditos()));
        cbEstado.setSelectedItem(e.getEstado());
    }

    private String obtenerCodigo() {
        String codigo = txtCodigo.getText() != null ? txtCodigo.getText().trim() : "";
        if (!codigo.isEmpty()) {
            return codigo;
        }

        int fila = tblMatriculas.getSelectedRow();
        if (fila >= 0) {
            return modeloTabla.getValueAt(fila, 0).toString();
        }

        mostrarError("Indique el código de matrícula o seleccione un registro en la tabla.");
        return null;
    }

    private void seleccionarFilaPorCodigo(String codigo) {
        for (int i = 0; i < modeloTabla.getRowCount(); i++) {
            String c = modeloTabla.getValueAt(i, 0).toString();
            if (c.equalsIgnoreCase(codigo)) {
                tblMatriculas.setRowSelectionInterval(i, i);
                tblMatriculas.scrollRectToVisible(tblMatriculas.getCellRect(i, 0, true));
                return;
            }
        }
    }

    private void activarAcciones(boolean activo) {
        mnuRegistrar.setEnabled(activo);
        mnuEditar.setEnabled(activo);
        mnuEliminar.setEnabled(activo);
        mnuBuscar.setEnabled(activo);
        btnRegistrar.setEnabled(activo);
        btnEditar.setEnabled(activo);
        btnEliminar.setEnabled(activo);
        btnBuscar.setEnabled(activo);
    }

    private void mostrarError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Atención", JOptionPane.ERROR_MESSAGE);
    }

    private void initComponents() {
        setJMenuBar(construirMenu());

        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel lblTitulo = new JLabel("Gestión de Matrícula", SwingConstants.CENTER);
        lblTitulo.setFont(lblTitulo.getFont().deriveFont(Font.BOLD, 20f));

        JPanel panelNorte = new JPanel(new BorderLayout(10, 10));
        panelNorte.add(lblTitulo, BorderLayout.NORTH);
        panelNorte.add(construirPanelFormulario(), BorderLayout.CENTER);

        panelPrincipal.add(panelNorte, BorderLayout.NORTH);
        panelPrincipal.add(construirPanelTabla(), BorderLayout.CENTER);
        panelPrincipal.add(construirPanelInferior(), BorderLayout.SOUTH);

        setContentPane(panelPrincipal);
        setMinimumSize(new Dimension(720, 560));
        pack();
    }

    private JMenuBar construirMenu() {
        JMenuBar jMenuBar1 = new JMenuBar();

        JMenu menuArchivo = new JMenu("Archivo");
        mnuNuevo = new JMenuItem("Nuevo");
        mnuNuevo.addActionListener(evt -> limpiar());
        mnuSalir = new JMenuItem("Salir");
        mnuSalir.addActionListener(evt -> cerrar());
        menuArchivo.add(mnuNuevo);
        menuArchivo.addSeparator();
        menuArchivo.add(mnuSalir);

        JMenu menuMatriculas = new JMenu("Matrículas");
        mnuRegistrar = new JMenuItem("Registrar");
        mnuRegistrar.addActionListener(evt -> registrar());
        mnuEditar = new JMenuItem("Editar");
        mnuEditar.addActionListener(evt -> editar());
        mnuEliminar = new JMenuItem("Eliminar");
        mnuEliminar.addActionListener(evt -> eliminar());
        mnuBuscar = new JMenuItem("Buscar");
        mnuBuscar.addActionListener(evt -> buscar());
        menuMatriculas.add(mnuRegistrar);
        menuMatriculas.add(mnuEditar);
        menuMatriculas.add(mnuEliminar);
        menuMatriculas.addSeparator();
        menuMatriculas.add(mnuBuscar);

        JMenu menuAyuda = new JMenu("Ayuda");
        mnuAcerca = new JMenuItem("Acerca de");
        mnuAcerca.addActionListener(evt -> JOptionPane.showMessageDialog(
                this,
                "Práctica Programada #3\nGestión de Matrícula\nSC-303 Programación Cliente/Servidor Concurrente\nGrupo 5",
                "Acerca de",
                JOptionPane.INFORMATION_MESSAGE));
        menuAyuda.add(mnuAcerca);

        jMenuBar1.add(menuArchivo);
        jMenuBar1.add(menuMatriculas);
        jMenuBar1.add(menuAyuda);
        return jMenuBar1;
    }

    private JPanel construirPanelFormulario() {
        JPanel panel = new JPanel(new BorderLayout(5, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Datos de Matrícula"));

        JPanel campos = new JPanel(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(4, 4, 4, 4);
        gc.fill = GridBagConstraints.HORIZONTAL;

        txtCodigo = new JTextField(12);
        txtNombre = new JTextField(20);
        txtIdentificacion = new JTextField(12);
        txtCreditos = new JTextField(5);
        cbCurso = new JComboBox<>(CursoEstudiante.values());
        cbEstado = new JComboBox<>(EstadoMatricula.values());

        gc.gridx = 0;
        gc.gridy = 0;
        campos.add(new JLabel("Código de Matrícula:"), gc);
        gc.gridx = 1;
        campos.add(txtCodigo, gc);
        gc.gridx = 2;
        campos.add(new JLabel("Identificación:"), gc);
        gc.gridx = 3;
        campos.add(txtIdentificacion, gc);

        gc.gridx = 0;
        gc.gridy = 1;
        campos.add(new JLabel("Nombre:"), gc);
        gc.gridx = 1;
        gc.gridwidth = 3;
        campos.add(txtNombre, gc);
        gc.gridwidth = 1;

        gc.gridx = 0;
        gc.gridy = 2;
        campos.add(new JLabel("Curso:"), gc);
        gc.gridx = 1;
        campos.add(cbCurso, gc);
        gc.gridx = 2;
        campos.add(new JLabel("Créditos (1 a 6):"), gc);
        gc.gridx = 3;
        campos.add(txtCreditos, gc);

        gc.gridx = 0;
        gc.gridy = 3;
        campos.add(new JLabel("Estado:"), gc);
        gc.gridx = 1;
        campos.add(cbEstado, gc);

        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        btnRegistrar = new JButton("Registrar");
        btnRegistrar.addActionListener(evt -> registrar());
        btnEditar = new JButton("Editar");
        btnEditar.addActionListener(evt -> editar());
        btnBuscar = new JButton("Buscar");
        btnBuscar.addActionListener(evt -> buscar());
        btnEliminar = new JButton("Eliminar");
        btnEliminar.addActionListener(evt -> eliminar());
        btnLimpiar = new JButton("Limpiar");
        btnLimpiar.addActionListener(evt -> limpiar());
        toolbar.add(btnRegistrar);
        toolbar.add(btnEditar);
        toolbar.add(btnBuscar);
        toolbar.add(btnEliminar);
        toolbar.add(btnLimpiar);

        panel.add(campos, BorderLayout.CENTER);
        panel.add(toolbar, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel construirPanelTabla() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Listado de matrículas"));

        tblMatriculas = new JTable();
        JScrollPane scroll = new JScrollPane(tblMatriculas);
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    private JPanel construirPanelInferior() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 8));
        btnActualizar = new JButton("Actualizar tabla");
        btnActualizar.addActionListener(evt -> refrescarTabla());
        btnSalir = new JButton("Salir");
        btnSalir.addActionListener(evt -> cerrar());
        panel.add(btnActualizar);
        panel.add(btnSalir);
        return panel;
    }
}
