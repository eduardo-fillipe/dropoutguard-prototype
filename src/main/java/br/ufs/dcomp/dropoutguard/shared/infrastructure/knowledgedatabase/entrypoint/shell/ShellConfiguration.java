package br.ufs.dcomp.dropoutguard.shared.infrastructure.knowledgedatabase.entrypoint.shell;

import jakarta.validation.constraints.Size;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
public class ShellConfiguration {

    @ShellMethod("Change Password. Shows support for bean validation.")
    public String changePassword(@Size(min = 8) String password) {
        return "Password changed";
    }
}
