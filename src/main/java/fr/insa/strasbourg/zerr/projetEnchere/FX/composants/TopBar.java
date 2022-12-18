/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.strasbourg.zerr.projetEnchere.FX.composants;

import fr.insa.strasbourg.zerr.projetEnchere.FX.JavaFXUtils;
import fr.insa.strasbourg.zerr.projetEnchere.FX.vues.FenetrePrincipale;
import fr.insa.strasbourg.zerr.projetEnchere.FX.vues.VueLogin;
import fr.insa.strasbourg.zerr.projetEnchere.FX.vues.VueNouvelleAnnonce;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.geometry.Insets;
import javafx.geometry.Side;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

/**
 *
 * @author jules
 */
public class TopBar extends HBox {

    private FenetrePrincipale main;
    private TextField tfRecherche;
    private BoutonIcon bRecherche;
    private Button bLogout;
    private Button bNouvelleAnnonce;

    private MenuItem milogout;
    private MenuItem miEnchere;
    private MenuItem miAnnonce;

    private MenuButton menuUser;

    public TopBar(FenetrePrincipale main) {
        ImageView imageView = this.getIcon("user.png");
        imageView.setFitHeight(25);
        imageView.setFitWidth(25);
        this.milogout = new MenuItem("Déconnection");
        this.miAnnonce = new MenuItem("Mes Annonces");
        this.miEnchere = new MenuItem("Mes Encheres");
        this.milogout.setOnAction((t) -> {
                t.consume();
                Alert confirmer = new Alert(Alert.AlertType.CONFIRMATION);
                ButtonType oui = new ButtonType("Oui");
                ButtonType non = new ButtonType("Non");
                confirmer.setTitle("Attention");
                confirmer.setHeaderText("Etes-vous sur de vous déconnecter ? ");
                confirmer.getButtonTypes().clear();
                confirmer.getButtonTypes().addAll(oui, non);
                Optional<ButtonType> select = confirmer.showAndWait();
                if (select.get() == oui){
                               doLogout();
                } else if (select.get() == non){
                }
        });

        ContextMenu cm = new ContextMenu();

        this.menuUser = new MenuButton();
        this.menuUser.setGraphic(imageView);
        this.menuUser.getItems().addAll(this.miAnnonce, this.miEnchere, this.milogout);
        this.menuUser.setPopupSide(Side.TOP);
        this.menuUser.setGraphicTextGap(200);
        this.menuUser.setPrefSize(50, 50);
//        this.menuUser.setVisible(false);
//        this.miAnnonce.setVisible(true);
        StackPane sp = new StackPane();
        this.menuUser.setOpacity(0);
        this.menuUser.setStyle("-fx-background-color: #ff0000; ");
        Circle cr = new Circle(30, 30, 30);
        cr.setFill(new ImagePattern(getImage("user.png")));
        cm.getItems().addAll(miAnnonce, miEnchere, milogout);
        cr.setOnMouseClicked((t) -> {
            System.out.println("bib bib");
            cm.show(cr, t.getScreenX(), t.getScreenY());
        });

        cr.setOnContextMenuRequested((ContextMenuEvent event) -> {
            cm.show(cr, event.getScreenX(), event.getScreenY());
        });

        this.main = main;
        this.setId("topbar");
        this.bNouvelleAnnonce = new Button("Déposer une annonce");
        this.bNouvelleAnnonce.setMaxWidth(Integer.MAX_VALUE);
        TopBar.setHgrow(this.bNouvelleAnnonce, Priority.SOMETIMES);

        this.tfRecherche = new TextField("Recherche");
        this.tfRecherche.setMaxWidth(Integer.MAX_VALUE);
        TopBar.setHgrow(this.tfRecherche, Priority.SOMETIMES);

        //this.bRecherche = new BoutonIcon("icones/recherche.png", 20, 20);
        String imag2= "iVBORw0KGgoAAAANSUhEUgAAAgAAAAIACAYAAAD0eNT6AAA5M0lEQVR4Xu3dB7gtZXm38T_Sq4J0lSZYEKkq3QOiookhlqDGCGLBFg0ajURROaImqARFE3tBwBqNGkVpgoqKCCoWuggCglQR6e373ifjkcP7rL3PLms988zM_buu-7r03Ye995pZs2btNTPvSP21VukJpReVDikdVTqudGbpt6Xr_9z_IyKiwXWP7t0PXKJm32D7CNtX2D7D9h27l9YUUluttGfpbWpW4BXyK5uIiGgu2T7lW6WFpSeXVhVac7_S9mpWxumlu-RXGBER0SSyfc5ppbeWHqdmn4QJe1Tp0NLv5FcIERFRG11eOqK0S2kpYWzWKR1Q-pX8QiciIsrUBaUD1ey7MEfblT5TukN-ARMREWXu9tIxpW2EGdujdLL8wiQiIupi31ZzRQGmsHPpFPkFR0RE1IfsjcBOwl88RM11l3ZNZr2wiIiI-tbXSxtrwFYovaN0q_zCISIi6nO3qJm7ZnkNjF0qca78AiEiIhpSF6qZtbb3Viy9X3zcT0REtCjbJ75PzSfjvWST-Jwl_8CJiIhIOru0tXrm-aWb5R8sERER3ZudF_di9cBypQ_JP0AiIiKauv9Ssw_tpFVKx8s_KCIiIlpyNime3e22U2wO5J_IPxgiIiKaeWeW1lZHbFQ6X_5BEBER0ey7qLSpkttCzW0R61-eiIiI5t6VSnyFwFalP8j_0kRERDT_bB-7rZLZpHSF_C9LRERE4-saNfPqpGAn_NlUhvUvSUREROPP_uDeSC1bVZztT0REFN05pdXVEpugwO5tXP9SRERENPlsH7ysWvBh-V-GiIiI4vqAgu0j_0sQERFRfC9UkC3V3Kyg_gWIiIgoPrvZ3sSvDLB7Ff9c_ocTERFRe_1KzT56Yt4v_0OJiIio_Q7XhCwo3SP_A4mIiKj97i7tojFbXs01h_UPIyIiojydpzEfCniH_A8hIiKifB2sMbF5_rt-1v9NpeNL7yntX3q8msdl0xivIgDA0Nhrv-0DbF-wQM2-4bDSCWr2GfV-pEvdUtpAY_Al-W_ehc4vLSztrJZmSgIAdJLtM-xY-ttKF8jvX7rQ5zVPtgDqb5o5e9djMxTuKAAAxmOn0kfVrU_D7aT9HTQPJ8t_04z9qXREaX0BADAZa6v5ZPkG-f1QxuxwxpzsIf_NsmXvcI5Ss1IAAIjwwNJH1I1L43fTHJwi_40yZZcl7ioAANqxoHSu_P4pUydqlraT_yaZsr_6VxYAAO1aUc35AfV-KlPbaBY-I_8NMnRbaV8BAJCL3ZHP9lH1fitDR2uG7LrIO-S_QdvZiX5PEgAAOe1W-qP8_qvtbtcMz5U7UP4_brurStsKAIDcbF91tfx-rO1eryVYqnSh_H_YZvZuip0_AKAr7Dy6G-X3Z21m9wiwffyUbLKD-j9qM_vYgo_9AQBds7vynRPwWE3jffL_QZtxwh8AoKteJL9fazO718FI9yv9Tv4_aKtPCQCAbrPL1uv9W1tdqikOA2wv_4_byib5WUkAAHSb3XXQjr_X-7m2svMTnIXy_7CNbGrF3QQAQD_YrLVZpg0-SCOcLv8P2-iTAgCgX46R39-10fdVWa10l_w_jM4um5jRZAUAAHTIuqWb5Pd70d2p5rDEX-w54h-10bsEAEA__Yf8fq-N7G6_f3HIiH8Q3a2l9QQAQD_ZpwC3yO__ojtYizluxD-I7sMCAKDfPia__4vuWC3myhH_ILodBABAv-0sv_-L7jL92VojvhjdBZpicgIAAHrmfPn9YHQPLP3ffMX1F6I7WAAADMPb5feD0dncBHrxiC9EZzchAgBgCBbI7weje0Gp9Xcidl3kcgIAYBhsn9f2nAALS63fqOB4AQAwLCfK7w8jO7KkE0Z8IbL3CACAYTlcfn8Y2bdK-smIL0T2EgEAMCwvk98fRvbjki4Z8YXIHi9gfO5X2lTN9NYvLx2q5jCXTXzxo9KFpYtK15au_3M2D4aNnavmRhlfK31CzTGyF6g5Ycdm8AKAcdlNfn8Y2W9Kum7EFyKzF2tgrjZTs5O2mSRPK_1J_jk2rq5Wc9zu3aWnixtXAZi7h8m_xkR2TWmiL5gzib-sMBs2cdXzS58pXSX_fIruvNL7S08trSgAmBm79039ehKZ3X1Xt4_4QmQrC5iebSivKp1aulv-OZQlu8nHl0rPLq0kAJia3ZK3fg2JzPb9bjA6pgDGKMuWnqXmRlV3yT9vsmfX-H6ytKMAwLN9X_26Edk9GjEYHbC4ddTcnjrDDarG1S9L-5dWEADcq36tiM4NRAcYO5nPbpN5q_xzpC_9vvSW0gMEAP41Ijo3EB2GbYPSR0p3yj83-pqdfGOXJ95fAIasfm2Izg1Eh2Gyv4JtJqy2T0JtM7us0CYDWVoAhqh-TYjODUSHYbGJemynZzu_-rkw1H6uZrIhAMNSvxZE5waiw3DYpE8nyz8HqDkj12YsfKAADEX9OhCdG4gO_WcfcR9Uuk1-_dN9s6sf9hKAIai3_-jcQHTotw1L35Vf7zR99mkAk2QB_VZv99G5gejQX39X-qP8OqeZdU5pcwHoq3qbj84NRIf-sY_87TI3O65dr2-aXXavDptaGED_1Nt7dG4gOvSLXdt-gvx6prlnb6T-TUybDfRNva1H5waiQ388qPQz-XVM48luNMQdB4H-qLfx6NxAdOiHrdSv-fuz9n0xlTDQF_X2HZ0biA7dt13pWvl1S5Ppp6W1BKDr6m07OjcQHbptFzVz29frlSbb2aV1BaDL6u06OjcQHbprm9If5NcpxWS3GWbmQKC76m06OjcQHbrJjvlfL78-KbYzxV0Fga6qt-fo3EB06B472_8y-XVJ7fSd0vIC0DX1thydG4gO3bKamrvX1euR2u2zYp4AoGvq7Tg6NxAdusNm-Dtefh1Sjt4kAF1Sb8PRuYHo0B3vkl9_lKe7S08VgK6ot-Ho3EB06IZnibn9u9B1pY0EoAvq7Tc6NxAd8nuIOOO_S51RWlYAsqu33ejcQHTI7X6lU-TXG-XuEAHIrt5uo3MD0SG3N8qvM8rfXaWdBCCzeruNzg1Eh7weVrpVfp1RNzqvtIIAZFVvs9G5geiQk330_z359UXdaqEAZFVvr9G5geiQ00vk11UXu6D0xdLbS_9Q2rG0ZWmT0hqltf_8v7ct7VZ6aemw0jdKV8t_v651e-nhApBRvb1G5waiQz42t_xV8uuqC_2pdExpXzVXL8yHzaxnbxYOKJ2g5rh6_fO6kL2ZAZBPva1G5waiQz7vkV9P2fu2mp3-ypocuwfCG0rnyv_87D1FALKpt9Po3EB0yGWD0m3y6yljNvPd_5S2Uyw7P-KZpZ_I_05Z-4Wa3xtAHvV2Gp0biA65fFR-HWXs5NIWat9epYvkf7-MPUcAMqm30ejcQHTI46GlO-TXUaauVHMyXyYrqpl4J_snJ3bowm7oBCCHehuNzg1Ehzw-LL9-MvX10gOV1yNLv5T_vTO1twBkUW-f0bmB6JDDWqVb5NdPhu5Ucz17F45h26cBH5N_DFn6sQBkUW-f0bmB6JDDW-XXTYZuLD1R3fOvynv3xF0FIIN624zODUSH9i1Tulx-3bSd3dp2B3WXXZaY8ZyKzwlABvW2GZ0biA7t-1v59dJ2NgvfI9R9dsw92wRCdrLimgLQtnrbjM4NRIf2HSu_XtrMPvZ_jPrj5fKPse1eKwBtq7fL6NxAdGiXzYVvJ9nV66Wt7CPzLh7zX5K3yT_WNrNJjAC0q94uo3MD0aFdr5BfJ232evWT3Vfgq_KPt802E4A21dtkdG4gOrTrO_LrpK3sUITtKPtq9dIl8o-7rQ4SgDbV22R0biA6tMduh5vlBLXfK_ckP-OyQHkuDzxDANpUb5PRuYHo0J7nyq-PttpHw_Ep-cffRnYzpXUEoC31NhmdG4gO7fm0_Ppoo--p3x_91-yTjmvll0MbDemNF5BNvT1G5waiQ3syTP5jH4dvpeF5jfyyaKOjBKAt9fYYnRuIDu3YWH5dtJGdGT9EK5SukF8e0dmtjAG0o94eo3MD0aEdz5dfF220o4bL7hdQL482Wl8A2lBvi9G5gejQjg_Ir4vovqthW610k_xyie6ZAtCGeluMzg1Eh3acKr8uonuhcLT8coluoQC0od4Wo3MD0SGenXH_B_l1EdktpfsLT5ZfNtH9jwC0od4Wo3MD0SHeBvLrIbrPC2ZpNZMg1csnsgsFoA31thidG4gO8faQXw_RvUhY5DPyyycyuxnUMgIQrd4Wo3MD0SHei-XXQ3R2GSIaL5FfPtFtJADR6u0wOjcQHeIdIr8eIrtYWFyGORl2F4Bo9XYYnRuIDvE-Ib8eIuOkM6_tkzJtXggAsertMDo3EB3ifU1-PUT270LtdPnlFNkBAhCt3g6jcwPRId735ddDZPsJNZuTv15OkdlhIQCx6u0wOjcQHeKdLb8eIttNqL1NfjlF9p8CEK3eDqNzA9Eh3q_l10Nk2wq118kvp8g-JgDR6u0wOjcQHeJdKr8eIttMqL1UfjlFxm2BgXj1dhidG4gO8dqeeY67z3l_L7-cIvuCAESrt8Po3EB0iHel_HqI7MFCbR_55RTZ5wQgWr0dRucGokO8S-TXQ2SPFGqvlF9OkR0pANHq7TA6NxAd4l0gvx4ie5xQO1B-OUX2EQGIVm-H0bmB6BDv5_LrIbKnCLVD5ZdTZO8TgGj1dhidG4gO8U6WXw-RvUqo2fTI9XKK7M0CEK3eDqNzA9Eh3hfl10NkHxBqbU_O9AoBiFZvh9G5gegQ74Py6yGyE4XFLV26TX45Rba3AESrt8Po3EB0iPdG-fUQmd35znZ6aDxWfhlFt72QwTql55QOU3NYyN4sn1b6Rumjaj6p2fwv_xpdV2-H0bmB6BDvufLrITrb6aHR9hUAlu140I6lSk9Xs7O_S37djMqu5Hl96QFCl9XrNTo3EB3i7SC_HqKznR4ax8svn8huUrMTQrwdSz-VXycz7Vo1J9XeT-iien1G5waiQ7w15NdDdN8VzGqlW-SXT2S2A0Is22G_vXS3_PqYS3Zlz7pC19TrMTo3EB3a0fYNge4pbSy8SH7ZRHekEGk5Teayz4tLjxC6pF6H0bmB6NAOO6moXhfRvUU4RX65RGe3IkYM-8v_K_LrYFxdVXq00BX1-ovODUSHdtjHj_W6iO4iDftqgE01vo-A59MThCj_Jr_8x901pW2ELqjXXXRuIDq046ny66KNnqfh-rj88ojuztIqQgTb5uzQV70OJpFdasulnfnV6y06NxAd2mGXD2X46_McDfMM5oeUbpdfHtH9WIhgt8C2v8zr5T_JblBzlQHyqtdZdG4gOrSn7ZsCLcomPhkau_tevRza6HBh0pYpnSq_7CO6sbSrkFW9vqJzA9GhPe-RXx9tdJmG9TH0dpr5hC-Tbk9h0v5dfrlHdnPpiUJG9bqKzg1Eh_bYyV_1-mird2sY7HDHj-QffxvZ_AMrCpNk21iGQ222rp8sZFOvp-jcQHRoj12P_Ef5ddJGd2gY0wP_s_xjb6tjhUlaX81lefVyb6tbS38lZFKvo-jcQHRo1zHy66StfqtmlsK-sjc4GU78W9R-wqTYJz0nyS_ztrPn3zOELOr1E50biA7t2kt-nbTZ_6qf89LbVRe_kX-8bWU7gj6_2WrbIfLLPEt26eeQL7_NpF430bmB6NCu5dVcM1yvlzazF88-sWX8bfnH2WZfFyZlD-U47j9d9ibA7gqKdtXrJTo3EB3a90H59dJ2r1E_2EfBX5B_fG33TGES1i5dIb-8M2ZXouwntKleJ9G5gejQPps2tF4vbWd_Qe2rbrOdf5br_Rfv96VlhXHLetx_umw7sxtSoR31-ojODUSHHGxGuHrdtJ1Nm2pnzXeRfez_RfnHlKF3CZNgN7eql3UXsu3s5UIb6nURnRuIDjk8X37dZMkmLOrSTYNWV75j_ouyY78bCuO2QHkmd5pL9ibg1UK0ej1E5waiQw72kbBdhlevnyydoua66uweo-Yuh_XvnyW77BPjtVbpcvll3cUOEiLVyz86NxAd8ni9_PrJlB27zjp1rX1C8Trlus5_VNsK42TH_Y-TX85d7o1ClHrZR-cGokMeK6vZydbrKFt2CZvdTS8L26lmmd53ur4mjJvtLOvl3IcOFSLUyz06NxAdcsk0Ve102RTG_1paVe3ZqPQx5b_m27JjvFsL47SLmnMq6mXdl94pTFq9zKNzA9EhF7s5TJeOZ15XOlixs9o9rPQpNfcvqH-frNkVCRifB5YulV_OfWsoN-lqS728o3MD0SEfu_6-Xk_Zu03NoYG9NZlr3O-vZrmcqOav6frnZ87OS9hMGBebqvqr8su5r31I_ZyeO4N6WUfnBqJDPraxny6_rrqSfSrw36VXlB6hub142ZuIHdScFW2X9Nmd1Oqf05XsfvQYn-wny04imy10LtsRplcv5-jcQHTIaXt149j2TLq59NPS59Wc3HRg6ZVq_qK3WdD-qfSm0uFqTpQ7X936eH-67HBOm-dJ9I29KezLc2O22fkudtUDxqdextG5geiQ1_vl1xd1K279Oj7Z7ujYRp8tLSOMS718o3MD0SEvuyxw6C94Xe5LwrjYx9__I7-Mh5jd3Io3AeNRL9vo3EB0yO1J6t5JbyRdXVpXGJcD5JfxkLOrSiZxsu3Q1Ms1OjcQHfKzY-P1eqO82Ru2vYRxsemds8_w2EbfKK0gzEe9TKNzA9EhP7uz3Vny645y9gFhXOzyz8z3dmg7exNgrw-Ym3p5RucGokM3bFS6Vn79Ua7s8k1ekMfHTnqrlzHdt-PVTCCG2auXZXRuIDp0x9PUn0sD-5i9QdtIGJd_lF_GNLrvllYRZqtejtG5gejQLW-WX4fUfnaMejdhXLZRM7tkvZxp6uxNAHNOzE69DKNzA9Ghez4lvx6pveykvxcI42J_yZ4nv5xpyZ1RWl2YqXr5RecGokP32OU_J8mvS2qnhcI4fU5-GdPMs_NQeBMwM_Wyi84NRIduWqn0Pfn1SbFxxv94vUx-GdPs-1lpTWFJ6uUWnRuIDt1ll0idKb9OKaYjxdzs47RF6Rb55Uxz65zSesJ06mUWnRuIDt1m90XnTUB8nygtLYyLHfc_V345Z8lmdvzJiPHs_UrMSDmdenlF5waiQ_fZJwHfl1-3NJns1qz85T9en5Zfzlmykzz_Rs129sMRX8-e3V3zQcIo9bKKzg1Eh36wGwcdK79-aXzZjuBgYdzsltD1ss7Uv-teq6mbb7YvLD1EqNXLKTo3EB36wz6StpPS6nVM88-u899XGLdHlW6WX95Z-pH8TXfsBNwuXoVzSemhwuLqZRSdG4gO_fOa0p3y65rm1lWlBcK42Y70bPnlnSWb2XGqv5rtdz9R_r_J3m_Fm4DF1csnOjcQHfpp19IV8uubZpedYLmRMAmfkl_eWbLDPX-r6dk9H74m_99m78rS5oKpl010biA69Jed-PMd-XVOS852AO8XN_aZlH3kl3mm3qOZsefH_8r_99njTUCjXi7RuYHo0G9LlQ4Q91OfTXbJl914CZOxWelG-eWeJZtJbznNnJ0j8GX575M9O7S1pYatXibRuYHoMAzblc6SX_90375YWkeYFLtt7S_kl3uWri9tqNmzNwFfkP9-2btGzY2XhqpeHtG5gegwHPYi9cbSrfLPg6F3eWkvYdI-Ir_ss2SHfZ6uubOrcI6R_77Zszc9j9Uw1csiOjcQHYbHzmw-Ss0LXv18GFp2aOQINdd3Y7KeLb_8M_VezZ9NEPVJ-e-dvRtKO2p46uUQnRuIDsO1m5rbh9bPiSF0t5qPbDcWImQ_7m_bwWyO-0_H3gRk_qRjqmz92NVDQ1Ivg-jcQHTAE9XNec7nml2_va0Qxc6Uz_z8sr9-N9F42cm375P_WdmzSZns9WAo6scfnRuIDjD2V4vNd_5d-edIH7qjdHRpayHaf8mvj0z9nSbD3gQcLv_zsmdvAp6kYagfe3RuIDqgZicEHancU7TONJsMyeZyn2pGN0yW7VzrdZIpe3MyaYfK_9zs2YnCf6X-qx93dG4gOmAqdvezV6q5A1qXThi8rfRVNWd0LyO0xT5Wt4_X6_WTpZ-WVlCMt8v__OzZdtT3K2PqxxydG4gOmIkNS_9S-kHpLvnnUdvdpGZaVrthj71xQbvshLrMJ5jaCW92YmKkt8j_HtmzQ2fPUn_Vjzc6NxAdMFtrlJ5T-njpXLXz6YC9MJ2mZspWO2mJKXtzyX4C3HPVjgPlf5fs2Y3F2lpek1Y_1ujcQHTAfK2pZurcN6m5tM7u8GYfH9bPtbn2x9KP1bzhsGmNH69mRjnkZIde2nhTONPsEr02vVa5l8-o7FM_u39D39SPMzo3EB0wCXYGtN2MaBc1nxbYuQRvVTPZir0AW59Tc2b-ov9vf83bTIUvUbMTsemL7dMGdMdGamaWq19nsvRz5Xjz-I_q3psAmzvjxeqX-jFG5waiA4BxsBMu7RyR-jUmS3aeyCOVh73RtZ1q_Xtmzt60vFr9UT--6NxAdAAwDofJv75kyk4QzeZ5ao6x179r5uxNwGvUD_Vji84NRAcA82XXjGf-SPsTystOsOvamwDrIHVf_ZiicwPRAcB8PFjNbWXr15Ys2UmpKym3vdVc2VL_7tmz83q6rH480bmB6ABgruy4_6nyrytZshnttlI32JU047x6Jiqb6bCr6scSnRuIDgDmKvs0ty9UtzxVzZuW-nFk713qpvpxROcGogOAuXiKcp_FbpeZdtGepVvkH0_27CTQrqkfQ3RuIDoAmK11SlfKv55k6YLSququBaU_yT-u7H1IzRwgXVH__tG5gegAYDbs1tEnyb-WZMk-Qu_DbZ93VXPPgvrxZc8m9bLnSBfUv3t0biA6AJiN7He221_9sbOaqbDrx5g9m7a7C28C6t87OjcQHQDM1O7KeTfIRdm9KPrGpsS-Tv6xZu-zyn877vp3js4NRAcAM2HH_a-Qfw3J0oWl1dRP25SulX_M2bM3ZJnfBNS_b3RuIDoAWBL7OPcE-dePLNn189uq3-y8hqvlH3v2_ru0rHKqf9fo3EB0ALAkB8u_dmTK7jY5BHYzo9_JP_7sfaO0gvKpf8_o3EB0ADCdBcp93N_-whySh5cul18O2fum8r0JqH_H6NxAdAAwlbWU-y_OS0praHg2K10mvzyyd3xpReVR_37RuYHoAGAUO-5_nPxrRpbs5jk7aLg2LF0kv1yy993SKsqh_t2icwPRAcAob5J_vcjUAcIGaq5-qJdN9uwGUhlmaqx_r-jcQHQAULO_rDPfnvbr6taUs5O0rppbHtfLKHtnlFZXu-rfKTo3EB0ALM6OqV8i_1qRpUtLDxQWZ3M0_FJ-WWXvTLW7LuvfJzo3EB0ALGJ_VX9N_nUiS3eWdhJGWbv0C_lllr2fldZUO-rfJTo3EB0ALPIG-deITL1OmI59pG4frdfLLXvnlNZTvPr3iM4NRAcA5nGl2-VfI7J0rDjuPxMPKJ0uv_yyd25pfcWqf4fo3EB0AGB_OV4s__qQJbvmva2Pibvo_qXT5Jdj9s4vPUhx6p8fnRuIDsCw2V_VX5F_bciSHfffRZitlUsnyy_P7F1c2lgx6p8dnRuIDsCwvVb-dSFT_yrM1Uqlk-SXafYuKT1Uk1f_3OjcQHQAhuuxyn3c_1tqZiTE3NmbgMx3cpwqu9xzU01W_TOjcwPRARgmO1nsN_KvCVn6vZpJbjB_yyv35Z1TdWVpc01O_fOicwPRARimL8u_HmTp7tIewjgtp9znekyVvRHcQpNR_6zo3EB0AIbn1fKvBZl6szAJyyr3G7-puqq0pcav_jnRuYHoAAyLvZDeIv9akKVTSksLk2LL9mj55Z6969WcszJO9c-Izg1EB2A47Das58m_DmTJ_tJrY0a4obE3AUfKL__s_aG0vcan_v7RuYHoAAzHMfKvAVmy4_5PEqLY1RWflF8P2buhtKPGo_7e0bmB6AAMw8vlt_9MvU2IZpNA_af8usjeTaXdNX_1943ODUQHoP8erdzH_b8jjvu3xd4EHCG_TrJ3c-mJmp_6e0bnBqID0G923N9utFJv-1m6WvE3gcF92ZuAw-XXTfbsTe2TNXf194vODUQHoN-Okt_us2TH_fcUsjhEfh1lz2ay3EtzU3-v6NxAdAD668Xy23ym3ilkc6D8esqevQl4hmav_j7RuYHoAPTTo9QcJ623-SzZ7WptYhrk08U3AXbXyOdpdurvEZ0biA5A_9itYM-W396zZJO6bChk9nr59Za9u0r7aObq_z46NxAdgP45Un5bz9I9pb8VuuAVatZXvQ4zZ28C9tPM1P9tdG4gOgD98vfy23mm3i10ycvUnKxZr8fM2ZuWV2rJ6v8uOjcQHYD-eFjpRvntPEunq7krHbrlJermmwC76dV06v8mOjcQHYB-WKF0lvw2niU77r-R0FV2gp2daFev18zZm4DXaGr1v4_ODUQHoB8-Jr99Z8leiJ8udN1z1b03AdZBGq3-d9G5gegAdN-z5bftTL1X6Iu9S3fIr-PsvVVe_W-icwPRAei2TUt_lN-2s3SGOO7fN08r3Sa_rrN3qO6r_np0biA6zJ_No712afU_t8x9vwxMjB33_5n8dp0lu3_7JkIf_Y26-SZg8dkn669F5waiw-wtr-bJf6Sak67qu6zZ_7dZzuw2m3a3KrvvNjAJH5TfpjP1LKHPnlq6VX69Z2_Rpaj1eHRuIDrM3Gqld5RukF-O0_Wb0hvUvHEAxsWOxdbPtUzZG2D0n_2Rk3nK6al634ix6NxAdJgZu6nKNfLLbzadX9pDwPw9VLN_IxrZT8Qb3iHZrfQn-ecBTZ8biA7TsxexcV9eZX8Z8eKIubLnzpnyz6ss2QmJdmIihmVn5T4ZNWNuIDpM7QGl78svs3H009JmAmbv_fLPp0w9Rxiq7dWc-Fk_J2h0biA6jGZn89u0pfXyGmc2Zetsb1-JYbPLrzLfnOXDwtBtU7pW_rlBPjcQHTy7pO_n8stqUh1VWknA9DYoXSf__MnSL0srCpC2Ll0t_xyh--YGosN9rVv6lfxymnR27_YtBIy2bOmH8s-bLN1UeqSAe9nz4Qr55wrdmxuIDveyv7AulF9GUdn8AXbXLaB2uPzzJVP7CPAeXrpc_vlCTW4gOjQ2VnO9fr182sgOCawsoPFXyn3c_-MCpmYnO18m_7yhEQPRQXqE8r1LPbf0aGHoHqz5zz8xyexwGeevYEk2LF0k__wZem4guqHLfLKKTbF5gDBUdk-JSV2GOo7suP_mAmam7UOsGXMD0Q3ZdurG5Sp2SGAVYWjeJf9cyNR-AmbHTrK2E57r59JQcwPRDVXXZq06r7SVMBR2k5W75Z8HWfqcgLlZR80lo_Vzaoi5geiGaIGaSXjqZZE9DgkMw4OU-7j_BaVVBcydzbXyC_nn1tByA9ENzVPkb9_btb6sZppi9M_SpW_Lr_Ms2ZtQO28GmC-bbfUM-efYkHID0Q2JTaPaxXtXj-piNfNuo1_sdtP1us7U_gLGx_6QmfSU65lzA9ENxbNLd8g__i53mzgk0Ce7l-6SX89Z-oKA8bt_6TT559sQcgPRDYHdcOdO-cfel76i5uM0dJedGJV52lS7fGs1AZNhE5-dIv-863tuILq-e6lyn009ri4p7Sh00f1KJ8qv0yzZJ03bCpgsexNwkvzzr8-5gej67JXKPYXquLNPOQ4sLSV0yUL5dZmpVwiIYbNKniD_HOxrbiC6vrIdYf1Yh9LXSmsIXbBAuY_7_7eAWMureQ2rn4t9zA1E10dD3vkv6lI1kx0hL7sW-nfy6y5Lv1ZzghYQbTk15zbVz8m-5Qai6xP76Psw-cc41OyQwEI1x5iRi62T4-XXWZbsipkdBLRnWTVzntTPzT7lBqLrC9v5v0_-8VFzgpmdZY48DpJfT5n6JwHts4mxjpZ_fvYlNxBdH9iT5BPyj43uze7HvYuQwa7KfVnq18WJpMjDXt-PlH-e9iE3EF3X2ZPj0_KPi3wcEmifnZz5W_l1kyU7d-SBAnKx16xPyj9fu54biK7L7ESRL8k_Jpo-u9bWbsuJWPZXdeazm-24_04CcrLt5z_ln7ddzg1E11V2qchX5R8PzayrSk8SImW_OuWfBeRmbwKOkH_udjU3EF0XDW2yiEll158vVHMYBZNlN27KfC-KY8Vxf3SDPU8Pl38OdzE3EF3X2HSRmW-X2sVOLq0nTIrdp-Fi-eWeJTtBdE0B3XKI_HO5a7mB6LrEbh35Q_nHQPPv6tKewrjZXyuZJzSxE0O5OgRdlf2w2pJyA9F1hf0V9WP535_Gl903wY6v2QQcGA87rl4v50zZCyjQZV1-E-AGousCm8TmF_K_O02m75TWF-brsaXb5Zdvlr4lLglFP7xe_vndhdxAdNnZsemz5X9vmmzXlJ4qzJUdrvqN_HLN0pXiUlD0i921smt3f3UD0WW2oZobktS_M8XEIYG5yzyH-d2lJwjon5epeX7Xz_msuYHosnqYmlnJ6t83ezeNGOt6p5YeLMzUq-WXYabeLKC_XqruvAlwA9Fl9Ajlvk3qVNkbls1K-5b-NOLrXe7a0l8LS7Jl6Vb55Zclu-STeR_Qd89T7vttLMoNRJfN1mouSat_z-xdXHqo7vXw0s9H_Lsut-iQgE3BDG-V0nnyyy1LvxfzPWA4nqv8bwLcQHSZbKfmL836d8yeveiP-oh8BfVr2spF2eWYmwi1z8gvqyzZR6JM_Yyh2Vu5Z-B0A9FlYZOR_FH-98veOVryJXPPKv1B_r_tcjeo2bjQsDOQ62WUqYUChulppdvkt4kMuYHoMligbh4z_6lmPoWqndT4M_nv0fU-Ig4JPLp0i_yyydJ3xHF_DJtd0pzx3Bw3EF3bbMVkfvGcqjPU3Nt9NuwOhn08JHCm7nv-w5DYcf9z5ZdJluyuj0v6hAoYApvqPNu-xg1E16bMH81M1_dKq2nunlG6Xv77djk7fGMn3QzN0fLLIkt23P_JArDIAuX6tNkNRNeW5yj3yRlTdYqav_rma8PSafLfv-sdVVpRw7C__OPP1DsFoLZr6Ub57aWN3EB0bfgH5b88Y1R2z3Q7s39cbIa9Q9W96SuXlJ0bYfMh9NmjSjfLP_Ys2ZtLZnAERttZOU46dwPRRevifM3WlzS5k904JNAtK6u5-qN-zFmy-ziMuiwVwL22l992onMD0UX6R3Vz5_-50jKarA1KP5D_2V3PDgmspH45Uv5xZsm2r70EYCbq7Sc6NxBdlK7es_ljirtlqr3JWKjuzGM90-xujluoH_aTf3yZepcAzFS9_UTnBqKLsFD-53ahDylu57-4v1E3Z0ScLrv85iXqNpvLIcvJQ6M6XZM7TAX0Ub0NRecGopukpUr_If8zu9B71C47hmt34at_r65nhwTsGHrX2MmfZ8k_nizZOSQbCcBs1NtRdG4gukmxnX9XJ72xM_Mz6OshAZs4x2bP65KPyz-OLNlx_6cLwGzV21J0biC6SbBpRz8h_7O60FuVzx6lK-V_1y5n03IeoG6wOSvq3z9ThwvAXNTbUnRuILpxs53_p-V_Tvbsr6jXKq91SifK_95dzw4JjGNipUnZVDmuF54quzMjx_2Buam3p-jcQHTjZC9EX5b_Gdmznf-rlJ-9uVpYukv-MXQ5u53yVsrHjvvbpEb175slu8PkxgIwV_U2FZ0biG5c7EY3X5X__tmznekL1S27l66QfyxdLuMhgQ_L_56ZeqYAzEe9TUXnBqIbB5vo5QT575092_k_X920duk4-cfU9ewTpAeofXvL_26Zer8AzFe9XUXnBqKbLzt-e7L8983e7er-X1B2pYVNsNS3QwIXq5mmsy12a-Mb5H-vLP1c470nBTBU9bYVnRuIbj7sL7Uu3tHObkHcp-lSF5R-J_84u5ytozYOCdihrJ_I_z5ZsluZPlwAxqHevqJzA9HN1RpqzkCuv1_2bio9Uf2zVumb8o-3632ltLrifED-d8iU3UkTwHjU21d0biC6ubBL0n4h_72yZx_r7qT-skMC9lfzHfKPvctdUtpRk2dTMGe-WZVNTQ1gfOptLDo3EN1sPaR0gfz3yZ5dMtXmceVI9jgvll8GXe5ONec72JucSbC7MV4n_3OzZG-4VxSAcaq3s-jcQHSzsWHp1_LfI3tXKed15pO0Zukb8sui631NzeGncVq29EP5n5UlO-7_CAEYt3pbi84NRDdTdie0y-T_--zZFLp9uRXtbC06JGBXPNTLpctdWtpZ4_Ne-Z-RqX0EYBLqbS06NxDdTDxS3TzL_LelzYTHln4jv3y6nB0SWKj53675r5X7uL_dhAjAZNTbW3RuILol2aZ0jfx_l72LS5sIi9y_9EX55dT17P4IdlLqXNj5LNfKf88s_UrNJFsAJqPe5qJzA9FN5zHK_QI5VTa3_IOFWl8PCdihqV00O3ar5e_Lf68s2eWqmwvAJNXbXXRuILqp2Atq5rugTdU5pfWF6Wynbp7MOV2zPSTwbvnvkakXCMCk1dtddG4gulF2U3Pmcf1vs2czuNnZ71iy1Uqfl1-GXe_bpXU1vacq93H_zwpAhHrbi84NRFezF0e7M1v977J3hsZ_edgQ7Fu6RX55djm77PNJGs0ODWU-p-X80qoCEKHe_qJzA9EtzmZCsznY63-Tve-JF8352LZ0ofxy7XJ2g6SFpaV1Lzvub8-V-t9myd54D22-CqBN9TYYnRuIbpHnqjmOWn89e6eouSMh5sfeQNlHz_Xy7Xp2p8r11Pi3EV_P1EsEIFK9DUbnBqIzL1I3byn7v2ru3obxebm6eQhoumwyqINKd4_4WpY-IwDR6u0wOjcQ3cuU-4VxquyadpvCFeNnl5_ZNej1MqfJZIdf7KRMALHqbTE6NxBd5rOhp-po3ffYLsbPDqscI7_sabzZpy1bC0Ab6u0xOjdA0_cxzfxab8yfXSVgk9LU64HGkx1yAdCOenuMzg3Q1Nn90Nn5x7M70dntaOv1QfPLDmMBaE-9TUbnBmh0NnMb2mP3oj9Cfr3Q3LKZGO3-DADaU2-X0bkB8h0qZGG3pu3iLJGZuqO0gwC0rd42o3MDdN_eImTz8NJZ8uuKZtarBSCDetuMzg1Qk12d8BohqxXEIYG59HU1d2UE0L56-4zODVCz83-V0AXPKv1Bfh2S77fifhVAJvU2Gp0bGHo2I-F-Qpc8rPQz-XVJ92bH_XcUgEzq7TQ6NzDkbOf_fKGLbEpmDglM3WsFIJt6O43ODQy120vPELrO1uH18ut3yB0rjvsDGdXbanRuYIjZ_ej3FPpiw9Jp8ut5iF1WWlMAMqq31-jcwNCyaWafKPSN3ajJ5m_o4r0mxpXdXntnAciq3majcwND6obSTkKf7VW6Tn7dD6E3CEBm9TYbnRsYSnaceHthCDYo_UD-OdDnvinuWwFkV2-30bmBIXRVaSthSJYpLSzdLf986FuXi-P-QBfU2250bqDvXVnaQhiqp5WulX9e9CW7lPUJAtAF9fYbnRvoczYT2qbC0D24dKr886MPHSQAXVFvv9G5gb52cWkTAY0-HhI4ubS0AHRFvQ1H5wb62HmlBwnw9lBzWKh-znSt35fWE4Auqbfj6NxA3zpbvDBieuuUTpB_7nQl-xSDuSyA7qm35ejcQJ_6iTgbGjNjH50vVHMSXf08yt7BAtBF9bYcnRvoSz8Wtz7F7O1eukL--ZS174jj_kBX1dtzdG6gD323tKqAuVm7dJz88ypbNp_F-gLQVfU2HZ0b6Hp2JvQqAubH7p53oPIeErDj_k8WgC6rt-vo3ECX-0ZpBQHjs6D0O_nnWtu9QwC6rt6uo3MDXe2Lau4AB4zbWmrm1q-fc231PTXzGADotnrbjs4NdLHPihdETJYdEjigdIf88y8yu7Oh3dwIQPfV23d0bqBrfVTc9QxxHqdmVsn6eRjRPWpubwygH-ptPDo30KU-KHb-iGdzS9j5JvXzcdIdKgB9Um_j0bmBrvQuAe2xQwL_orhDAt8X57gAfVNv59G5gS7EX0LI4rGli-Sfo-PMrkLgXhZA_9TbenRuIHtvEZDL_dVchVI_V8eR3ahocwHoo3p7j84NZM1OgHq1gJzskMBrSrfLP3fn2lmljQWgr-ptPjo3kDGb9Wx_AfltrWbHXT-HZ5PNPvje0koC0Gf1th-dG8iWvRjuI6A77OY8LyydL_98nq5bSx8pbSYAQ1C_BkTnBjJlZ1jvLaCb7LDAgtLhpTNLt8k_xy8vHaPmE651mv8MwEDUrwfRuYEs2Yslk56gT-wNge3kN1FzbH_l-34ZwMDU-73o3ECGbhZ3OgMA9Fu974vODbTdTaU9BABAv9X7v-jcQJvdUNpJAAD0X70PjM4NtNX1am60AgDAENT7wejcQBtdVdpSAAAMR70vjM4NRGdTnT5KAAAMS70_jM4NRPdQAQAwPPX-MDo3EB0AAENU7w-jcwPRAQAwRPX-MDo3EB0AAENU7w-jcwPRAQAwRPX-MDo3EB0AAENU7w-jcwPRAQAwRPX-MDo3EB0AAENU7w-jcwPRAQAwRPX-MDo3EB0AAENU7w-jcwPRAQAwRPX-MDo3EB0AAENU7w-jcwPRAQAwRPX-MDo3EB0AAENU7w-jcwPRAQAwRPX-MDo3EB0AAENU7w-jcwPRAQAwRPX-MDo3EB0AAENU7w-jcwPRAQAwRPX-MDo3EB0AAENU7w-jcwPRAQAwRPX-MDo3EB0AAENU7w-jcwPRAQAwRPX-MDo3EB0AAENU7w-jcwPRAQAwRPX-MDo3EB0AAENU7w-jcwPRAQAwRPX-MDo3EB0AAENU7w-jcwPRAQAwRPX-MDo3EB0AAENU7w-jcwPRAQAwRPX-MDo3EB0AAENU7w-jcwPRAQAwRPX-MDo3EB0AAENU7w-jcwPRAQAwRPX-MDo3EB0AAENU7w-jcwPRAQAwRPX-MDo3EB0AAENU7w-jcwPRAQAwRPX-MDo3EB0AAENU7w-jcwPRAQAwRPX-MDo3EB0AAENU7w-jcwPRAQAwRPX-MDo3EB0AAENU7w-jcwPRAQAwRPX-MDo3EB0AAENU7w-jcwPRAQAwRPX-MDo3EB0AAENU7w-jcwPRAQAwRPX-MDo3EB0AAENU7w-jcwPRAQAwRPX-MDo3EB0AAENU7w-jcwPRAQAwRPX-MDo3EB0AAENU7w-jcwPRAQAwRPX-MDo3EB0AAENU7w-jcwPRAQAwRPX-MDo3EB0AAENU7w-jcwPRAQAwRPX-MDo3EB0AAENU7w-jcwPRAQAwRPX-MDo3EB0AAENU7w-jcwPRAQAwRPX-MDo3EB0AAENU7w-jcwPRAQAwRPX-MDo3EB0AAENU7w-jcwPRAQAwRPX-MDo3EB0AAENU7w-jcwPRAQAwRPX-MDo3EB0AAENU7w-jcwPRAQAwRPX-MDo3EB0AAENU7w-jcwPRAQAwRPX-MDo3EB0AAENU7w-jcwPRAQAwRPX-MDo3EB0AAENU7w-jcwPRAQAwRPX-MDo3EB0AAENU7w-jcwPRAQAwRPX-MDo3EB0AAENU7w-jcwPRAQAwRPX-MDo3EB0AAENU7w-jcwPRAQAwRPX-MDo3EB0AAENU7w-jcwPRAQAwRPX-MDo3EN1SAgBgWGzfV-8PI7unpDtGfCGylQQAwLCsIr8_jOz2km4a8YXI1hEAAMOynvz-MLIbS7puxBci21QAAAzLw-T3h5FdU9IlI74Q2a4CAGBYdpPfH0Z2UUlnjvhCZPsLAIBhebn8_jCy00s6bsQXIjtMAAAMy3vl94eRHVvSUSO-ENnxAgBgWE6S3x9GdmRJh4z4QmQ3l5YTAADDYPs82_fV-8PIDi7pxSO-EN0uAgBgGHaT3w9Gt29Ju4_4QnRvEwAAw_BO-f1gdP93Bd6aI74Q3YViSmAAQP_Zvs4uwav3g9GtoT-7YsQXo9tRAAD0m_3lXe__ortUi_nWiH8Q3UcFAEC_fVx-_xfd_10CuMjCEf8guttK6wsAgH6y-f9vld__RfcWLeZJI_5BG71HAAD0U9uT_yzKTv7_i9VKd434R9H9SdwdEADQP_YJd9vX_lt3llZW5Ufy_7CNPi0AAPrlc_L7uzY6VSMcLP8P2-geVR9PAADQYY9Xs2-r93dt9CaN8Dj5f9hW52rERxQAAHTMKqXz5fdzbbWtRrhf6XL5f9xWnxUAAN3W9g33Fs-u_59y0r0sZygu6oUCAKCb9pffr7XZtFfa7SD_H7TZ7aUnCwCAbnmCmvlt6v1am22nadhHAxfI_0dt9kdNccwCAICEHlO6UX5_1mbnaAbeIP8ftt3VWsI7FwAAErCdv-2z6v1Y271OM2B3B8z2sYVlkwTtKQAAcrKP_e1T63r_1XZ2OH0tzdAx8t8gQ_bG5EUCACAXO-HPdrT1fitDdiXCjG2pPJMWjOpoNddWAgDQJtsXZf2jeVFba5a-Lf9NMnVeaTcBANAO-8g_0yQ_ozpec2DT8dbfKFv2KYW981pXAADEsBv72GR19T4pY7tqjrJ_CrCom0pHlB4kAAAmY-3SocpxV7-ZdJzmYSf5b5i5W0sfLe2saaY7BABghmxfskvp48p5hdxU2Sfkdo-fefmi_DfuQr8uvV3NXZiWEwAAM7N8aUHpnaWL5PcvXegzGoONSrfIf_MuZR_XnFT6j9LL1Jw8uFlpvdKqAgAMjb322z7A9gV2zpvtGw5Xs6_oykf8U2W__0M0Jm-T_wFERESUrzdrjOzjkLPlfwgRERHl6Vw1--yx2rF0t_wPIyIiovazfbSdBD8R75P_gURERNR-h2mC7GOFs-R_KBEREbXXL0sraMK2UPevCiAiIupLNhneIxXkH-R_ASIiIorvBQr2X_K_BBEREcVl5-aFs9n1TpT_ZYiIiGjy2T54WbVkpdIP5X8pIiIimlw2N88D1DK7O9IF8r8cERERjb_LSxsoCZtz-FL5X5KIiIjG19WlzZXMlqU_yP-yRERENP-uL22tpLZR8-6k_qWJiIho7l2lxDv_RR6h5vhE_csTERHR7LtSzSR8nfAgMWUwERHRfPtVaUN1zKqlb8k_GCIiIlpyJynBpX5zZRMUfED-QREREdHU2Qx_rU3yM07PK90s_wCJiIjo3m4t7aeesTsV_VT-wRIREVFzS99Hq6eWLx1eulv-gRMREQ0x2ycepmYf2Xs7qZnHuF4IREREQ8qm0t9NA2PvdBaWbpFfIERERH3Ozot7s5o76w6WzRlwVOke-QVERETUt75e2kj4ix3U3Nu4XlBERER96PjS44QpLRBvBIiIqD_Zjn9XYcbspgdHl26XX5hERESZs32XHd7eSpiz1UsvLf1CfgETERFl6rzSgaW1hbF6lJorB34tv9CJiIja6NLSEaVdSksJE2ULeDs1l1D8oHSn_AohIiKaRLbPObX0ptK2YqffqlVKe5QOLh1bukx-hREREc0l-wvf9i1vLT2htLKQ2hqlx5deoOawwadL3yydXvpN6fo_x7TERETD6y7dux-wfYLtG2wfcaSafca-as7ct_PQeun_A4fQT7pKk6rlAAAAAElFTkSuQmCC";

        this.bRecherche = new BoutonIcon("icones/recherche.png", 20, 20);
        this.bRecherche.setText("RE");

        this.bRecherche.setStyle("-fx-content-display: top;");

        //this.bRecherche.setText("Rechercher");
        this.bRecherche.setMaxWidth(Integer.MAX_VALUE);
        TopBar.setHgrow(this.bRecherche, Priority.SOMETIMES);

        Region region = new Region();
        TopBar.setHgrow(region, Priority.ALWAYS);

        this.bLogout = new Button("Déconnction");
        this.bLogout.setMaxWidth(Integer.MAX_VALUE);
        this.bLogout.setId("bouton-rouge");
        TopBar.setHgrow(this.bLogout, Priority.SOMETIMES);

        //JavaFXUtils.addSimpleBorder(region);

        this.setSpacing(10);
        this.setPadding(new Insets(10, 10, 10, 10));

        sp.getChildren().addAll(cr,menuUser);
        this.getChildren().addAll(bNouvelleAnnonce, this.tfRecherche, this.bRecherche, region, sp);

        

        this.bNouvelleAnnonce.setOnAction((t) -> {
            try {
                this.main.setCenter(new VueNouvelleAnnonce(this.main));
            } catch (IOException ex) {
                Logger.getLogger(TopBar.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

    }

    private void doLogout() {

        this.main.getSessionInfo().setCurUser(Optional.empty());
        this.main.setCenter(new VueLogin(this.main));
        this.main.setTop(null);
    }

    private ImageView getIcon(String resourcePath) {
        InputStream input //
                = this.getClass().getResourceAsStream(resourcePath);
        Image image = new Image(input);
        return new ImageView(image);
    }

    private Image getImage(String resourcePath) {
        InputStream input //
                = this.getClass().getResourceAsStream(resourcePath);
        Image image = new Image(input);
        return image;
    }
}
