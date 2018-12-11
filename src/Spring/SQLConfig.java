package Spring;

import Contract.ICourse;
import Contract.IDbContext;
import Contract.IRegistration;
import Contract.IUser;
import MySQL.DbContext;
import MySQL.MySQLRepositoryCourse;
import MySQL.MySQLRepositoryRegistrations;
import MySQL.MySQLRepositoryUsers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SQLConfig {

    //define a bean for DbContext
    @Bean
    public IDbContext dbContext() {
        return new DbContext();
    }


    //define bean for MySQLRepositoryCourse and inject DbContext
    @Bean
    public ICourse courseRepository() {
        return new MySQLRepositoryCourse(dbContext());
    }

    //define bean for MySQLRepositoryUsers and inject DbContext
    @Bean
    public IUser userRepository() {
        return new MySQLRepositoryUsers(dbContext());
    }

    //define bean for MySQLRepositoryUsers and inject DbContext
    @Bean
    public IRegistration registrationRepository() {
        return new MySQLRepositoryRegistrations(dbContext());
    }
}
