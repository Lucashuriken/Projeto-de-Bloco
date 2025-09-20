package org.bugsolver.config;

import org.bugsolver.model.Group;
import org.bugsolver.model.Ocorrency;
import org.bugsolver.model.User;
import org.bugsolver.model.enums.GravityLevel;
import org.bugsolver.model.enums.OcorrencyStatus;
import org.bugsolver.service.GroupService;
import org.bugsolver.service.OcorrencyService;
import org.bugsolver.service.UserService;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

@Configuration
public class DataInitializer {

    @Bean
    ApplicationRunner init(UserService userService, GroupService groupService, OcorrencyService ocorrencyService) {
        return args -> {
            // Verificar se já existem dados
            if (!userService.listar().isEmpty()) {
                return; // Dados já inicializados
            }

            // Criar usuários de exemplo
            User admin = new User();
            admin.setNome("Administrador");
            admin.setEmail("admin@bugsolver.com");
            admin.setSenha("admin123");
            admin = userService.salvar(admin);

            User dev1 = new User();
            dev1.setNome("João Silva");
            dev1.setEmail("joao@bugsolver.com");
            dev1.setSenha("dev123");
            dev1 = userService.salvar(dev1);

            User dev2 = new User();
            dev2.setNome("Maria Santos");
            dev2.setEmail("maria@bugsolver.com");
            dev2.setSenha("dev123");
            dev2 = userService.salvar(dev2);

            User tester = new User();
            tester.setNome("Carlos Tester");
            tester.setEmail("carlos@bugsolver.com");
            tester.setSenha("test123");
            tester = userService.salvar(tester);

            // Criar grupos de exemplo
            Group frontendGroup = new Group();
            frontendGroup.setNome("Front-end");
            frontendGroup.setDescricao("Equipe responsável pela interface do usuário");
            frontendGroup.setLiderId(dev1.getId());
            frontendGroup = groupService.criar(frontendGroup);

            Group backendGroup = new Group();
            backendGroup.setNome("Back-end");
            backendGroup.setDescricao("Equipe responsável pela lógica de negócio e APIs");
            backendGroup.setLiderId(dev2.getId());
            backendGroup = groupService.criar(backendGroup);

            Group qaGroup = new Group();
            qaGroup.setNome("Quality Assurance");
            qaGroup.setDescricao("Equipe de testes e garantia de qualidade");
            qaGroup.setLiderId(tester.getId());
            qaGroup = groupService.criar(qaGroup);

            Group suporteGroup = new Group();
            suporteGroup.setNome("Suporte");
            suporteGroup.setDescricao("Equipe de suporte ao cliente");
            suporteGroup.setLiderId(admin.getId());
            suporteGroup = groupService.criar(suporteGroup);

            // Criar ocorrências de exemplo
            Ocorrency bug1 = new Ocorrency();
            bug1.setTitulo("Erro de validação no formulário de cadastro");
            bug1.setDescricao("Quando o usuário tenta se cadastrar com um email inválido, a mensagem de erro não aparece corretamente.\n\nPassos para reproduzir:\n1. Acessar tela de cadastro\n2. Inserir email inválido\n3. Clicar em cadastrar\n\nResultado esperado: Mensagem de erro clara\nResultado atual: Página fica em branco");
            bug1.setAutorId(tester.getId());
            bug1.setGrupoId(frontendGroup.getId());
            bug1.setGravityLevel(GravityLevel.HIGH);
            bug1.setStatus(OcorrencyStatus.OPEN);
            bug1.setDataCriacao(LocalDateTime.now().minusDays(3));
            ocorrencyService.criar(bug1);

            Ocorrency bug2 = new Ocorrency();
            bug2.setTitulo("Performance lenta na consulta de relatórios");
            bug2.setDescricao("Os relatórios estão demorando mais de 30 segundos para carregar quando há muitos dados.\n\nDetalhes técnicos:\n- Query no banco está sem otimização\n- Falta índice na tabela de logs\n- Possível memory leak no processo");
            bug2.setAutorId(admin.getId());
            bug2.setGrupoId(backendGroup.getId());
            bug2.setGravityLevel(GravityLevel.MEDIUM);
            bug2.setStatus(OcorrencyStatus.OPEN);
            bug2.setDataCriacao(LocalDateTime.now().minusDays(1));
            ocorrencyService.criar(bug2);

            Ocorrency feature1 = new Ocorrency();
            feature1.setTitulo("Adicionar filtro de data nas ocorrências");
            feature1.setDescricao("Solicitação de melhoria para adicionar filtros de data na listagem de ocorrências.\n\nRequirements:\n- Filtro por data de criação\n- Filtro por data de fechamento\n- Range de datas (de/até)\n- Integração com os filtros existentes");
            feature1.setAutorId(dev1.getId());
            feature1.setGrupoId(frontendGroup.getId());
            feature1.setGravityLevel(GravityLevel.LOW);
            feature1.setStatus(OcorrencyStatus.OPEN);
            feature1.setDataCriacao(LocalDateTime.now().minusHours(6));
            ocorrencyService.criar(feature1);

            Ocorrency bug3 = new Ocorrency();
            bug3.setTitulo("Usuário não consegue fazer logout");
            bug3.setDescricao("Alguns usuários relataram que o botão de logout não funciona em determinados browsers.\n\nBrowsers afetados:\n- Safari 14.x\n- Firefox 89.x\n\nWorkaround: Fechar todas as abas do browser");
            bug3.setAutorId(tester.getId());
            bug3.setGrupoId(frontendGroup.getId());
            bug3.setGravityLevel(GravityLevel.HIGH);
            bug3.setStatus(OcorrencyStatus.CLOSED);
            bug3.setResponsavelId(dev1.getId());
            bug3.setDataCriacao(LocalDateTime.now().minusDays(7));
            bug3.setDataFechamento(LocalDateTime.now().minusDays(2));
            ocorrencyService.criar(bug3);

            Ocorrency support1 = new Ocorrency();
            support1.setTitulo("Cliente não recebe emails de notificação");
            support1.setDescricao("Cliente reportou que não está recebendo emails automáticos do sistema.\n\nInformações do cliente:\n- Email: cliente@empresa.com\n- Última notificação: há 1 semana\n- Verificar configurações SMTP");
            support1.setAutorId(admin.getId());
            support1.setGrupoId(suporteGroup.getId());
            support1.setGravityLevel(GravityLevel.MEDIUM);
            support1.setStatus(OcorrencyStatus.OPEN);
            support1.setDataCriacao(LocalDateTime.now().minusHours(2));
            ocorrencyService.criar(support1);

            System.out.println("✅ Dados de exemplo criados com sucesso!");
            System.out.println("👤 Usuários criados: " + userService.listar().size());
            System.out.println("👥 Grupos criados: " + groupService.listar().size());
            System.out.println("📋 Ocorrências criadas: " + ocorrencyService.listar().size());
            System.out.println("\n🔑 Credenciais de exemplo:");
            System.out.println("Admin: admin@bugsolver.com / admin123");
            System.out.println("Dev 1: joao@bugsolver.com / dev123");
            System.out.println("Dev 2: maria@bugsolver.com / dev123");
            System.out.println("Tester: carlos@bugsolver.com / test123");
        };
    }
}