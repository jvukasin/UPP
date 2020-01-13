package com.naucnacentrala.NaucnaCentrala.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Inheritance(strategy= InheritanceType.SINGLE_TABLE) //ovom anotacijom se naglasava tip mapiranja "jedna tabela po hijerarhiji"
@DiscriminatorColumn(name="type", discriminatorType= DiscriminatorType.STRING) //ovom anotacijom se navodi diskriminatorska kolona
public class User implements UserDetails {

    @Id
    private String username;

    @Column(name = "ime", nullable = false)
    private String ime;

    @Column(name = "prezime", nullable = false)
    private String prezime;

    @Column(name = "grad", nullable = false)
    private String grad;

    @Column(name = "drzava", nullable = false)
    private String drzava;

    @Column(name = "titula")
    private String titula;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "active", nullable = false)
    private boolean aktivan;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "user_nobl",
            joinColumns = @JoinColumn(name = "korisnik_id", referencedColumnName = "username"),
            inverseJoinColumns = @JoinColumn(name = "nau_obl_id", referencedColumnName = "sifra"))
    private List<NaucnaOblast> naucneOblasti;

    @ManyToMany(cascade =  {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "username"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    protected List<Role> roles;


    public User() {

    }

    public User(String username, String password, String ime, String prezime, String grad, String drzava, String titula, String email,List<NaucnaOblast> naucneOblasti) {
        this.username = username;
        this.password = password;
        this.ime = ime;
        this.prezime = prezime;
        this.email = email;
        this.grad = grad;
        this.drzava = drzava;
        this.titula = titula;
        this.naucneOblasti = naucneOblasti;
    }

    public User(User user){
        this(user.getUsername(), user.getPassword(), user.getIme(), user.getPrezime(),
                user.getEmail(), user.getGrad(), user.getDrzava(), user.getTitula(), user.getNaucneOblasti());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // uvek ima samo jednu rolu - uzmi privilegije i vrati
        if(!this.roles.isEmpty()){
            Role r = roles.iterator().next();
            List<Privilege> privileges = new ArrayList<Privilege>();
            for(Privilege p : r.getPrivileges()){
                privileges.add(p);
            }
            return privileges;
        }
        return null;
    }


    public String getIme() {
        return ime;
    }

    public String getPrezime() {
        return prezime;
    }

    public String getGrad() {
        return grad;
    }

    public String getDrzava() {
        return drzava;
    }

    public String getEmail() {
        return email;
    }

    public List<NaucnaOblast> getNaucneOblasti() {
        return naucneOblasti;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public void setPrezime(String prezime) {
        this.prezime = prezime;
    }

    public void setGrad(String grad) {
        this.grad = grad;
    }

    public void setDrzava(String drzava) {
        this.drzava = drzava;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setNaucneOblasti(List<NaucnaOblast> naucneOblasti) {
        this.naucneOblasti = naucneOblasti;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public String getPassword() {
        return password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAktivan() {
        return aktivan;
    }

    public void setAktivan(boolean aktivan) {
        this.aktivan = aktivan;
    }

    public String getTitula() {
        return titula;
    }

    public void setTitula(String titula) {
        this.titula = titula;
    }
}
