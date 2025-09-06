package br.org.na.csm.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SpaRedirectController {

    @GetMapping({
            "/",
            "/cadastrar",
            "/editar",
            "/efetuar-pedido",
            "/confirmar-pedido",
            "/logar"
    })
    public String redirect() {
        return "forward:/index.html";
    }
}

