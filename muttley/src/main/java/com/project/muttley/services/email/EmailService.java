package com.project.muttley.services.email;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {
  @Autowired
  private JavaMailSender javaMailSender;

  public String sendEmail(
      String from,
      String to,
      String subject,
      String content,
      byte[] file) {

    try {

      MimeMessage message = javaMailSender.createMimeMessage();

      MimeMessageHelper helper = new MimeMessageHelper(message, true);

      helper.setTo(to);
      helper.setSubject(subject);
      helper.setText(content, true);
      helper.setFrom(from);

      helper.addAttachment(
          "certificate.pdf",
          new ByteArrayResource(file));

      javaMailSender.send(message);

      return "Email enviado com sucesso!";

    } catch (Exception e) {
      throw new RuntimeException(
          "Falha ao enviar email",
          e);
    }
  }

  public String buildEmailHtmlParticipantion(
      String eventTitle,
      List<String> competencies) {

    String competenciesHtml = competencies.isEmpty()
        ? "<li>Não informado</li>"
        : competencies.stream()
            .map(c -> "<li>" + c + "</li>")
            .reduce("", (a, b) -> a + b);

    return """
        <!DOCTYPE html>
        <html>
        <head>
          <meta charset="UTF-8">

          <style>
            body {
              font-family: Arial, sans-serif;
              background-color: #f4f6f8;
              margin: 0;
              padding: 32px 16px;
              color: #333;
            }

            .container {
              max-width: 640px;
              margin: 0 auto;
              background: #ffffff;
              border-radius: 12px;
              overflow: hidden;
              box-shadow: 0 2px 10px rgba(0,0,0,0.08);
            }

            .header {
              background: #7c3aed;
              color: white;
              padding: 32px 24px;
              text-align: center;
            }

            .header h1 {
              margin: 0;
              font-size: 24px;
            }

            .content {
              padding: 32px 24px;
              line-height: 1.7;
              font-size: 15px;
            }

            .steps {
              margin-top: 16px;
              padding-left: 20px;
            }

            .steps li {
              margin-bottom: 10px;
            }

            .competencies {
              background: #f9fafb;
              padding: 16px;
              border-radius: 8px;
              margin-top: 24px;
            }

            .competencies ul {
              padding-left: 20px;
              margin: 12px 0 0 0;
            }

            .footer {
              background: #f9fafb;
              padding: 24px;
              text-align: center;
              font-size: 13px;
              color: #777;
            }

            .event-title {
              color: #7c3aed;
              font-weight: bold;
            }
          </style>
        </head>

        <body>

          <div class="container">

            <div class="header">
              <h1>Seu certificado de participação está disponível!</h1>
            </div>

            <div class="content">

              <p>Olá!</p>

              <p>
                Seu certificado de participação no evento
                <span class="event-title">%s</span>
                já está disponível em anexo.
              </p>

              <p>
                Para adicionar essa conquista ao seu perfil no LinkedIn, siga este passo a passo:
              </p>

              <ol class="steps">
                <li>Acesse seu perfil no LinkedIn.</li>
                <li>Vá até a seção <strong>Licenças e certificados</strong>.</li>
                <li>Clique em <strong>Adicionar licença ou certificado</strong>.</li>
                <li>Preencha as informações do certificado conforme constam no arquivo em anexo.</li>
                <li>No campo de organização emissora, informe <strong>Fatec Zona Leste</strong>.</li>
                <li>Na parte de competências, adicione manualmente as seguintes competências listadas abaixo.</li>
                <li>Salve o cadastro para exibir o certificado no seu perfil.</li>
              </ol>

              <div class="competencies">
                <strong>Competências do evento:</strong>

                <ul>
                  %s
                </ul>
              </div>

              <p style="margin-top: 24px;">
                Além disso, não deixe essa conquista apenas no perfil — publique o certificado no seu feed do LinkedIn.
                Esse é um passo importante para reforçar sua presença profissional, evidenciar suas competências e
                ampliar a visibilidade do seu perfil para recrutadores e contatos da área.
              </p>

              <p>
                Ao publicar, não se esqueça de mencionar também a <strong>Fatec Zona Leste</strong>.
              </p>

              <p style="margin-top: 32px;">
                Atenciosamente,<br/>
                <strong>Fatec Zona Leste</strong>
              </p>

            </div>

            <div class="footer">
              Este é um email automático de certificação.
            </div>

          </div>

        </body>
        </html>
        """.formatted(
        eventTitle,
        competenciesHtml);
  }

  public String buildEmailHtml(
      String eventTitle,
      List<String> competencies) {

    String competenciesHtml = competencies.isEmpty()
        ? "<li>Não informado</li>"
        : competencies.stream()
            .map(c -> "<li>" + c + "</li>")
            .reduce("", (a, b) -> a + b);

    return """
        <!DOCTYPE html>
        <html>
        <head>
          <meta charset="UTF-8">

          <style>
            body {
              font-family: Arial, sans-serif;
              background-color: #f4f6f8;
              margin: 0;
              padding: 32px 16px;
              color: #333;
            }

            .container {
              max-width: 640px;
              margin: 0 auto;
              background: #ffffff;
              border-radius: 12px;
              overflow: hidden;
              box-shadow: 0 2px 10px rgba(0,0,0,0.08);
            }

            .header {
              background: #7c3aed;
              color: white;
              padding: 32px 24px;
              text-align: center;
            }

            .header h1 {
              margin: 0;
              font-size: 24px;
            }

            .content {
              padding: 32px 24px;
              line-height: 1.7;
              font-size: 15px;
            }

            .steps {
              margin-top: 16px;
              padding-left: 20px;
            }

            .steps li {
              margin-bottom: 10px;
            }

            .competencies {
              background: #f9fafb;
              padding: 16px;
              border-radius: 8px;
              margin-top: 24px;
            }

            .competencies ul {
              padding-left: 20px;
              margin: 12px 0 0 0;
            }

            .footer {
              background: #f9fafb;
              padding: 24px;
              text-align: center;
              font-size: 13px;
              color: #777;
            }

            .event-title {
              color: #7c3aed;
              font-weight: bold;
            }
          </style>
        </head>

        <body>

          <div class="container">

            <div class="header">
              <h1>Seu certificado especial está disponível!</h1>
            </div>

            <div class="content">

              <p>Olá!</p>

              <p>
                Parabéns pela sua conquista! Seu certificado especial referente ao evento
                <span class="event-title">%s</span>
                já está disponível em anexo.
              </p>

              <p>
                Para adicionar essa conquista ao seu perfil no LinkedIn,
                siga este passo a passo:
              </p>

              <ol class="steps">
                <li>Acesse seu perfil no LinkedIn.</li>
                <li>Vá até a seção <strong>Licenças e certificados</strong>.</li>
                <li>Clique em <strong>Adicionar licença ou certificado</strong>.</li>
                <li>Preencha as informações conforme constam no certificado em anexo.</li>
                <li>No campo de organização emissora, informe <strong>Fatec Zona Leste</strong>.</li>
                <li>Adicione manualmente as competências abaixo.</li>
                <li>Salve o cadastro para exibir o certificado no seu perfil.</li>
              </ol>

              <div class="competencies">
                <strong>Competências do evento:</strong>

                <ul>
                  %s
                </ul>
              </div>

              <p style="margin-top: 24px;">
                Além disso, aproveite essa conquista para publicar o certificado especial no seu feed do LinkedIn!
                Compartilhar esse reconhecimento é uma forma direta de destacar sua participação,
                fortalecer sua imagem profissional e ampliar a visibilidade do seu perfil.
              </p>

              <p>
                Ao publicar, não se esqueça de mencionar também a
                <strong>Fatec Zona Leste</strong> no texto!
              </p>

              <p style="margin-top: 32px;">
                Atenciosamente,<br/>
                <strong>Fatec Zona Leste</strong>
              </p>

            </div>

            <div class="footer">
              Este é um email automático de certificação.
            </div>

          </div>

        </body>
        </html>
        """.formatted(
        eventTitle,
        competenciesHtml);
  }
}
