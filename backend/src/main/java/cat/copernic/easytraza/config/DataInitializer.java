package cat.copernic.easytraza.config;

import cat.copernic.easytraza.entities.AlbaraClient;
import cat.copernic.easytraza.entities.AlbaraProveidor;
import cat.copernic.easytraza.entities.Client;
import cat.copernic.easytraza.entities.LiniaAlbaraClient;
import cat.copernic.easytraza.entities.LiniaAlbaraProveidor;
import cat.copernic.easytraza.entities.LotProveidor;
import cat.copernic.easytraza.entities.MateriaPrimera;
import cat.copernic.easytraza.entities.ProducteFinal;
import cat.copernic.easytraza.entities.Proveidor;
import cat.copernic.easytraza.entities.Tracabilitat;
import cat.copernic.easytraza.entities.Usuari;
import cat.copernic.easytraza.enums.EstatAlbaraClient;
import cat.copernic.easytraza.enums.EstatLot;
import cat.copernic.easytraza.enums.RolUsuari;
import cat.copernic.easytraza.repository.AlbaraClientRepository;
import cat.copernic.easytraza.repository.AlbaraProveidorRepository;
import cat.copernic.easytraza.repository.ClientRepository;
import cat.copernic.easytraza.repository.LotProveidorRepository;
import cat.copernic.easytraza.repository.MateriaPrimeraRepository;
import cat.copernic.easytraza.repository.ProducteFinalRepository;
import cat.copernic.easytraza.repository.ProveidorRepository;
import cat.copernic.easytraza.repository.TracabilitatRepository;
import cat.copernic.easytraza.repository.UsuariRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Inicialitza dades mínimes de demostració quan l'aplicació arrenca.
 *
 * <p>Aquest inicialitzador crea només les dades que falten i evita duplicats
 * utilitzant camps únics com email, CIF, NIF, nom o número d'albarà.</p>
 *
 * <p>També crea un usuari superadministrador si no existeix. La contrasenya
 * inicial és temporal i queda registrada com a avís perquè s'ha de canviar
 * després del primer inici de sessió.</p>
 *
 * @author orjon
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    private static final String PASSWORD_DEMO = "12345678";
    private static final String SUPERADMIN_PASSWORD_TEMPORAL = "12345678";

    @Value("${app.superadmin.email:superadmin@easytraza.com}")
    private String superAdminEmail;

    private final UsuariRepository usuariRepository;
    private final ProveidorRepository proveidorRepository;
    private final ClientRepository clientRepository;
    private final MateriaPrimeraRepository materiaPrimeraRepository;
    private final ProducteFinalRepository producteFinalRepository;
    private final AlbaraProveidorRepository albaraProveidorRepository;
    private final LotProveidorRepository lotProveidorRepository;
    private final AlbaraClientRepository albaraClientRepository;
    private final TracabilitatRepository tracabilitatRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Constructor amb totes les dependències necessàries per crear dades demo.
     *
     * @param usuariRepository repositori d'usuaris
     * @param proveidorRepository repositori de proveïdors
     * @param clientRepository repositori de clients
     * @param materiaPrimeraRepository repositori de matèries primeres
     * @param producteFinalRepository repositori de productes finals
     * @param albaraProveidorRepository repositori d'albarans de proveïdor
     * @param lotProveidorRepository repositori de lots de proveïdor
     * @param albaraClientRepository repositori d'albarans de client
     * @param tracabilitatRepository repositori de traçabilitat
     * @param passwordEncoder codificador segur de contrasenyes
     */
    public DataInitializer(
            UsuariRepository usuariRepository,
            ProveidorRepository proveidorRepository,
            ClientRepository clientRepository,
            MateriaPrimeraRepository materiaPrimeraRepository,
            ProducteFinalRepository producteFinalRepository,
            AlbaraProveidorRepository albaraProveidorRepository,
            LotProveidorRepository lotProveidorRepository,
            AlbaraClientRepository albaraClientRepository,
            TracabilitatRepository tracabilitatRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.usuariRepository = usuariRepository;
        this.proveidorRepository = proveidorRepository;
        this.clientRepository = clientRepository;
        this.materiaPrimeraRepository = materiaPrimeraRepository;
        this.producteFinalRepository = producteFinalRepository;
        this.albaraProveidorRepository = albaraProveidorRepository;
        this.lotProveidorRepository = lotProveidorRepository;
        this.albaraClientRepository = albaraClientRepository;
        this.tracabilitatRepository = tracabilitatRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Executa la càrrega inicial de dades en arrencar l'aplicació.
     *
     * @param args arguments d'arrencada
     */
    @Override
    @Transactional
    public void run(String... args) {
        crearSuperAdmin();
        crearDadesDemo();
    }

    /**
     * Crea el superadministrador si encara no existeix.
     */
    private void crearSuperAdmin() {
        if (usuariRepository.findByEmail(superAdminEmail).isPresent()) {
            return;
        }

        Usuari superAdmin = new Usuari();
        superAdmin.setNom("Super Admin");
        superAdmin.setEmail(superAdminEmail);
        superAdmin.setPassword(passwordEncoder.encode(SUPERADMIN_PASSWORD_TEMPORAL));
        superAdmin.setRol(RolUsuari.ADMIN);
        superAdmin.setActiu(true);

        usuariRepository.save(superAdmin);

        logger.warn("S'ha creat el superadministrador amb una contrasenya temporal hardcodejada.");
        logger.warn("Cal canviar aquesta contrasenya immediatament des del perfil d'usuari.");
        logger.warn("Credencials temporals: {} / {}", superAdminEmail, SUPERADMIN_PASSWORD_TEMPORAL);
    }

    /**
     * Crea les dades de demostració demanades a l'enunciat.
     */
    private void crearDadesDemo() {
        Usuari admin = crearUsuariSiNoExisteix("orjona13@gmail.com", "Admin Demo", RolUsuari.ADMIN);
        crearUsuariSiNoExisteix("operari1@easytraza.com", "Operari Demo 1", RolUsuari.OPERARI);
        crearUsuariSiNoExisteix("operari2@easytraza.com", "Operari Demo 2", RolUsuari.OPERARI);

        List<Proveidor> proveidors = crearProveidorsDemo();
        List<Client> clients = crearClientsDemo();
        List<MateriaPrimera> materies = crearMateriesPrimeresDemo();
        List<ProducteFinal> productes = crearProductesFinalsDemo();

        crearAlbaransProveidorDemo(proveidors, materies, admin);
        crearAlbaransClientDemo(clients, productes);

        logger.info("Dades de demostració verificades correctament.");
    }

    /**
     * Crea un usuari si no existeix pel seu email.
     *
     * @param email email únic
     * @param nom nom visible
     * @param rol rol de l'usuari
     * @return usuari existent o creat
     */
    private Usuari crearUsuariSiNoExisteix(String email, String nom, RolUsuari rol) {
        return usuariRepository.findByEmail(email).orElseGet(() -> {
            Usuari usuari = new Usuari();
            usuari.setNom(nom);
            usuari.setEmail(email);
            usuari.setPassword(passwordEncoder.encode(PASSWORD_DEMO));
            usuari.setRol(rol);
            usuari.setActiu(true);
            return usuariRepository.save(usuari);
        });
    }

    /**
     * Crea sis proveïdors actius per a la demo.
     *
     * @return llista de proveïdors demo
     */
    private List<Proveidor> crearProveidorsDemo() {
        String[][] dades = {
            {"B66000001", "Farines del Vallès", "Carrer Farina 1", "farines@demo.com", "600000001"},
            {"B66000002", "Làctics Montseny", "Carrer Llet 2", "lactics@demo.com", "600000002"},
            {"B66000003", "Sucre i Cacau SL", "Carrer Dolç 3", "sucrecacau@demo.com", "600000003"},
            {"B66000004", "Granges del Nord", "Carrer Granja 4", "granges@demo.com", "600000004"},
            {"B66000005", "Fruits Secs Girona", "Carrer Fruits 5", "fruitssecs@demo.com", "600000005"},
            {"B66000006", "Olis Mediterranis", "Carrer Oli 6", "olis@demo.com", "600000006"}
        };

        List<Proveidor> proveidors = new ArrayList<>();

        for (String[] d : dades) {
            Proveidor proveidor = proveidorRepository.findByCif(d[0]).orElseGet(() -> {
                Proveidor p = new Proveidor();
                p.setCif(d[0]);
                p.setNom(d[1]);
                p.setAdreca(d[2]);
                p.setEmail(d[3]);
                p.setTelefon(d[4]);
                p.setObservacions("Proveïdor creat automàticament per a la demo.");
                p.setActiu(true);
                return proveidorRepository.save(p);
            });

            proveidors.add(proveidor);
        }

        return proveidors;
    }

    /**
     * Crea tres clients actius per a la demo.
     *
     * @return llista de clients demo
     */
    private List<Client> crearClientsDemo() {
        String[][] dades = {
            {"C66000001", "Forn Sant Jordi", "Barcelona", "client1@demo.com", "610000001"},
            {"C66000002", "Pastisseria Central", "Terrassa", "client2@demo.com", "610000002"},
            {"C66000003", "Cafeteria La Plaça", "Sabadell", "client3@demo.com", "610000003"}
        };

        List<Client> clients = new ArrayList<>();

        for (String[] d : dades) {
            Client client = clientRepository.findAll().stream()
                    .filter(c -> d[0].equalsIgnoreCase(c.getNif()))
                    .findFirst()
                    .orElseGet(() -> {
                        Client c = new Client();
                        c.setNif(d[0]);
                        c.setNom(d[1]);
                        c.setCognoms("");
                        c.setAdreca(d[2]);
                        c.setEmail(d[3]);
                        c.setTelefon(d[4]);
                        c.setRegistreSanitari("RGS-DEMO-" + d[0]);
                        c.setActiu(true);
                        return clientRepository.save(c);
                    });

            clients.add(client);
        }

        return clients;
    }

    /**
     * Crea vint matèries primeres actives per a la demo.
     *
     * @return llista de matèries primeres demo
     */
    private List<MateriaPrimera> crearMateriesPrimeresDemo() {
        String[] noms = {
            "Farina de blat", "Farina integral", "Sucre blanc", "Sucre morè",
            "Llevat fresc", "Llevat sec", "Sal fina", "Oli d'oliva",
            "Mantega", "Llet sencera", "Ous", "Cacau en pols",
            "Xocolata negra", "Ametlla molta", "Avellana", "Nous",
            "Mel", "Nata", "Vainilla", "Canyella"
        };

        List<MateriaPrimera> materies = new ArrayList<>();

        for (String nom : noms) {
            MateriaPrimera materia = materiaPrimeraRepository.findAll().stream()
                    .filter(m -> nom.equalsIgnoreCase(m.getNom()))
                    .findFirst()
                    .orElseGet(() -> {
                        MateriaPrimera m = new MateriaPrimera();
                        m.setNom(nom);
                        m.setDescripcio("Matèria primera creada automàticament per a la demo.");
                        m.setActiu(true);
                        return materiaPrimeraRepository.save(m);
                    });

            materies.add(materia);
        }

        return materies;
    }

    /**
     * Crea deu productes finals actius per a la demo.
     *
     * @return llista de productes finals demo
     */
    private List<ProducteFinal> crearProductesFinalsDemo() {
        String[] noms = {
            "Pa rodó", "Barra de pa", "Croissant", "Magdalena", "Coca dolça",
            "Pastís de xocolata", "Ensaïmada", "Galetes artesanes", "Brioix", "Pa integral"
        };

        List<ProducteFinal> productes = new ArrayList<>();

        for (String nom : noms) {
            ProducteFinal producte = producteFinalRepository.findAll().stream()
                    .filter(p -> nom.equalsIgnoreCase(p.getNom()))
                    .findFirst()
                    .orElseGet(() -> {
                        ProducteFinal p = new ProducteFinal();
                        p.setNom(nom);
                        p.setDescripcio("Producte final creat automàticament per a la demo.");
                        p.setActiu(true);
                        return producteFinalRepository.save(p);
                    });

            productes.add(producte);
        }

        return productes;
    }

    /**
     * Crea cinc albarans de proveïdor amb tres línies de lot cadascun.
     *
     * @param proveidors proveïdors disponibles
     * @param materies matèries primeres disponibles
     * @param admin usuari d'alta
     */
    private void crearAlbaransProveidorDemo(
            List<Proveidor> proveidors,
            List<MateriaPrimera> materies,
            Usuari admin
    ) {
        for (int i = 0; i < 5; i++) {
            String numeroAlbara = "AP-DEMO-" + (i + 1);

            if (albaraProveidorRepository.existsByNumeroAlbara(numeroAlbara)) {
                continue;
            }

            AlbaraProveidor albara = new AlbaraProveidor();
            albara.setNumeroAlbara(numeroAlbara);
            albara.setDataRecepcio(LocalDate.now().minusDays(15L - i));
            albara.setProveidor(proveidors.get(i % proveidors.size()));
            albara.setUsuariAlta(admin);
            albara.setLinies(new ArrayList<>());

            for (int j = 0; j < 3; j++) {
                MateriaPrimera materia = materies.get((i * 3 + j) % materies.size());

                LotProveidor lot = new LotProveidor();
                lot.setIdentificadorLot("LOT-DEMO-" + (i + 1) + "-" + (j + 1));
                lot.setProveidor(albara.getProveidor());
                lot.setMateriaPrimera(materia);
                lot.setQuantitat(100.0 + (j * 25.0));
                lot.setUnitat("kg");
                lot.setDataCaducitat(LocalDate.now().plusDays(30L + i + j));
                lot.setEstat(j == 0 && i < 2 ? EstatLot.OBERT : EstatLot.EN_ESTOC);
                lot.setAlbaraProveidor(albara);

                if (lot.getEstat() == EstatLot.OBERT) {
                    lot.setDataObertura(LocalDateTime.now().minusDays(2L + i));
                    lot.setUsuariObertura(admin);
                }

                LiniaAlbaraProveidor linia = new LiniaAlbaraProveidor();
                linia.setAlbaraProveidor(albara);
                linia.setMateriaPrimera(materia);
                linia.setQuantitat(lot.getQuantitat());
                linia.setUnitat(lot.getUnitat());
                linia.setLot(lot);

                albara.getLinies().add(linia);
            }

            albaraProveidorRepository.save(albara);
        }
    }

    /**
     * Crea cinc albarans de client amb cinc línies cadascun.
     *
     * @param clients clients disponibles
     * @param productes productes finals disponibles
     */
    private void crearAlbaransClientDemo(List<Client> clients, List<ProducteFinal> productes) {
        int existentsDemo = (int) albaraClientRepository.findAll().stream()
                .filter(a -> a.getClient() != null
                && a.getClient().getEmail() != null
                && a.getClient().getEmail().endsWith("@demo.com"))
                .count();

        if (existentsDemo >= 5) {
            return;
        }

        List<LotProveidor> lotsTraçabilitat = lotProveidorRepository.findAll().stream()
                .limit(4)
                .toList();

        for (int i = existentsDemo; i < 5; i++) {
            AlbaraClient albara = new AlbaraClient();
            albara.setClient(clients.get(i % clients.size()));
            albara.setData(LocalDateTime.now().minusDays(5L - i));
            albara.setEstat(i % 2 == 0 ? EstatAlbaraClient.LLIURAT : EstatAlbaraClient.PENDENT);
            albara.setLinies(new ArrayList<>());

            for (int j = 0; j < 5; j++) {
                LiniaAlbaraClient linia = new LiniaAlbaraClient();
                linia.setAlbaraClient(albara);
                linia.setProducte(productes.get((i + j) % productes.size()));
                linia.setQuantitat(10.0 + j);

                albara.getLinies().add(linia);
            }

            AlbaraClient guardat = albaraClientRepository.save(albara);
            crearTracabilitatDemo(guardat, lotsTraçabilitat);
        }
    }

    /**
     * Crea registres de traçabilitat per a les línies d'un albarà de client.
     *
     * @param albara albarà de client guardat
     * @param lots lots a associar a les línies
     */
    private void crearTracabilitatDemo(AlbaraClient albara, List<LotProveidor> lots) {
        if (lots.isEmpty()) {
            return;
        }

        for (LiniaAlbaraClient linia : albara.getLinies()) {
            for (LotProveidor lot : lots) {
                Tracabilitat tracabilitat = new Tracabilitat();
                tracabilitat.setLotProveidor(lot);
                tracabilitat.setProducteFinal(linia.getProducte());
                tracabilitat.setLiniaAlbaraClient(linia);
                tracabilitat.setDataRegistre(albara.getData());

                tracabilitatRepository.save(tracabilitat);
            }
        }
    }
}
