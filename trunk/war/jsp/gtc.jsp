<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
	   "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" lang="en_US" xml:lang="en_US">

 <head>
  <meta http-equiv="content-type" content="text/html; charset=UTF-8" />
  <title><bean:message bundle="systemConfig" key="application.name"/> - <bean:message key="gtc.titel" /></title>
  <link rel="stylesheet" type="text/css" href="jsp/import/styles.css" />
 </head>
 <body>
 
<tiles:insert page="import/header.jsp" flush="true" />

<div class="content">

<br />
<h3>General Terms and Conditions (Version 1):</h3>
<logic:present name="userinfo" property="benutzer.gtc">
	<logic:equal name="userinfo" property="benutzer.gtc" value="">
		<div id="italic"><bean:message key="gtc.comment" /></div> 
	</logic:equal>
</logic:present>

<p></p>
 
 
<p>

1. Agreement. In this Registration Agreement ("Agreement") "you" and "your" refer to each user of the software provided by doctor-doc.com, "we", us" and "our" refer to the "Verein Doctor-Doc", 
and "Services" refers to the functionalities within Doctor-Doc.com and provided by us. This Agreement explains our obligations to you, and explains your obligations to us for various Services.
</p>
<p>
2. FEES. Doctor-Doc is freely available in it's basic services as described on our webpage. For some services a fee will be applicable. As consideration for the services you have selected, 
you agree to pay to us the applicable service(s) fees. All fees payable hereunder are non-refundable. As further consideration for the Services, you agree to: (1) provide certain current, 
complete and accurate information about you as required by the registration process and (2) maintain and update this information as needed to keep it current, complete and accurate. 
All such information shall be referred to as account information ("Account Information"). You, by completing and submitting this Agreement represent that the statements in your application are true.
</p>
<p>
3. TERM. You agree that the Registration Agreement will remain in full force during the length of the term of your Registration for the service(s) of Doctor-Doc. Should you choose to renew or 
otherwise lengthen the term of your Registration, then the term of this Registration Agreement will be extended accordingly. This Agreement will remain in full force during the length of the 
term of your Registration as selected, recorded, and paid for upon registration for the service(s) of Doctor-Doc. Should you choose to renew or otherwise lengthen the term of your Registration, 
then the term of this Registration Agreement will be extended accordingly.
</p>
<p>
4. MODIFICATIONS TO AGREEMENT. You agree, during the period of this Agreement, that we may: (1) revise the terms and conditions of this Agreement; and (2) change the services provided under 
this Agreement. Any such revision or change will be binding and effective immediately on posting of the revised Agreement or change to the service(s) on our web site, or on notification to 
you by e-mail or regular mail as per the Notices section of this agreement. You agree to review our web site, including the Agreement, periodically to be aware of any such revisions. If you 
do not agree with any revision to the Agreement, you may terminate this Agreement at any time by providing us with notice by e-mail or regular mail as per the Notices section of this agreement. 
Notice of your termination will be effective on receipt and processing by us. You agree that, by continuing to use the Services following notice of any revision to this Agreement or change in 
service(s), you shall abide by any such revisions or changes.
</p>
<p>
5. MODIFICATIONS TO YOUR ACCOUNT. In order to change any of your account information with us, you must use your Account Identifier and Password that you selected when you opened your account 
with us. Please safeguard your Account Identifier and Password from any unauthorized use. In no event will we be liable for the unauthorized use or misuse of your Account Identifier or Password.
</p>
<p>
6. USE OF EXTERNAL DOCUMENT DELIVERY SERVICE. Doctor-doc does not provide any document delivery on it's own. You agree to be solely responsible for fullfilling the General Terms and Conditions 
for the use your subito-doc.de account, or any other external document delivery service within doctor-doc. All fees resulting from the use of your subito-doc.de account, or any other external 
document delivery service within doctor-doc, shall be paid directly by you.
</p>
<p>
7. COPYRIGHT-LAW. You agree to be solely responsible for fullfilling the copyright-law of your country by using the services of doctor-doc.
</p>
<p>
8. DISPUTES. For any dispute, you agree to submit to the jurisdiction of the courts of Olten, Switzerland.
</p>
<p>
9. ANNOUNCEMENTS. We reserve the right to distribute information to you that is pertinent to the quality or operation of our services. These announcements will be predominately informative 
in nature and may include notices describing changes, upgrades, new products or other information.
</p>
<p>
10. LIMITATION OF LIABILITY. You agree that our entire liability, and your exclusive remedy, with respect to any Services(s) provided under this Agreement and any breach of this Agreement is 
solely limited to the amount you paid for such Service(s). We and our contractors shall not be liable for any direct, indirect, incidental, special or consequential damages resulting from the 
use or inability to use any of the Services or for the cost of procurement of substitute services. Because some states do not allow the exclusion or limitation of liability for consequential or 
incidental damages, in such states, our liability is limited to the extent permitted by law. We disclaim any and all loss or liability resulting from, but not limited to: (1) loss or liability 
resulting from access delays or access interruptions; (2) loss or liability resulting from data non-delivery or data mis-delivery; (3) loss or liability resulting from acts of God; (4) loss or 
liability resulting from the unauthorized use or misuse of your account identifier or password; (5) loss or liability resulting from errors, omissions, or misstatements in any and all information 
or services(s) provided under this Agreement; (6) loss or liability resulting from the interruption of your Service. You agree that we will not be liable for any loss of your registration, or for 
interruption of business, or any indirect, special, incidental, or consequential damages of any kind (including lost profits) regardless of the form of action whether in contract, tort (including 
negligence), or otherwise, even if we have been advised of the possibility of such damages. In no event shall our maximum liability exceed the value of zero dot zero one (CHF 0.01) Switzerland Francs.
</p>
<p>
11. INDEMNITY. You agree to release, indemnify, and hold us, our contractors, agents, employees, officers, directors and affiliates harmless from all liabilities, claims and expenses, including 
attorney's fees, of third parties relating to or arising under this Agreement, the Services provided hereunder or your use of the Services, including without limitation infringement by you, or 
someone else using the Service with your computer, of any intellectual property or other proprietary right of any person or entity, or from the violation of any of our operating rules or policy 
relating to the service(s) provided. You also agree to release, indemnify and hold us harmless pursuant to the terms and conditions contained in the Dispute Policy. When we are threatened with suit 
by a third party, we may seek written assurances from you concerning your promise to indemnify us; your failure to provide those assurances may be considered by us to be a breach of your Agreement 
and may result in deactivation of your registration.
</p>
<p>
12. BREACH. You agree that failure to abide by any provision of this Agreement, any operating rule or policy or the Dispute Policy provided by us, may be considered by us to be a material breach 
and that we may provide a written notice, describing the breach, to you. If within thirty (30) calendar days of the date of such notice, you fail to provide evidence, which is reasonably satisfactory 
to us, that you have not breached your obligations under the Agreement, then we may delete the registration. Any such breach by you shall not be deemed to be excused simply because we did not act 
earlier in response to that, or any other breach by you.
</p>
<p>
13. DISCLAIMER OF WARRANTIES. You agree that your use of our Services is solely at your own risk. You agree that such Service(s) is provided on an "as is," "as available" basis. We expressly 
disclaim all warranties of any kind, whether express or implied, including but not limited to the implied warranties of merchantability, fitness for a particular purpose and non-infringement. 
We make no warranty that the Services will meet your requirements, or that the Service(s) will be uninterrupted, timely, secure, or error free; nor do we make any warranty as to the results that 
may be obtained from the use of the Service(s) or as to the accuracy or reliability of any information obtained through the Service or that defects in the Service will be corrected. You understand 
and agree that any material and/or data downloaded or otherwise obtained through the use of Service is done at your own discretion and risk and that you will be solely responsible for any damage to 
your computer system or loss of data that results from the download of such material and/or data. We make no warranty regarding any goods or services purchased or obtained through the Service or any 
transactions entered into through the Service. No advice or information, whether oral or written, obtained by you from us or through the Service shall create any warranty not expressly made herein.
</p>
<p>
14. INFORMATION. As part of the registration process, you are required to provide us certain information and to update us promptly as such information changes such that our records are current, 
complete and accurate. You are obliged to provide us the following information:
<br />
i) Your name and postal address
<br />
ii) Your phone number
<br />
iii) Your email
<br />
iv) Your Subito-Account information, including username, password
<br />
Any other information which we request from you at registration is voluntary. Any voluntary information we request is collected such that we can continue to improve the products and services 
offered to you.
</p>
<p>
15. DISCLOSURE AND USE OF REGISTRATION INFORMATION. You hereby consent to any and all such disclosures and use of, and guidelines, limits and restrictions on disclosure or use of, information 
provided by you in connection with the registration (including any updates to such information), whether during or after the term of your registration. You hereby irrevocably waive any and all 
claims and causes of action you may have arising from such disclosure or use of your registration information by us.

You may access your registration information in our possession to review, modify or update such information, by accessing your account, or similar service, made available by us.

We will not process data about any identified or identifiable natural person that we obtain from you in a way incompatible with the purposes and other limitations which we describe in this Agreement.

We will take reasonable precautions to protect the information we obtain from you from our loss, misuse, unauthorized access or disclosure, alteration or destruction of that information.
</p>
<p>
16. REVOCATION. Your wilful provision of inaccurate or unreliable information, your wilful failure promptly to update information provided to us, or your failure to respond for over fifteen calendar 
days to inquiries by us concerning the accuracy of contact details associated with the your registration shall constitute a material breach of this Agreement and be a basis for cancellation of the 
registration.
</p>
<p>
17. SEVERABILITY. You agree that the terms of this Agreement are severable. If any term or provision is declared invalid or unenforceable, that term or provision will be construed consistent with 
applicable law as nearly as possible to reflect the original intentions of the parties, and the remaining terms and provisions will remain in full force and effect.
</p>
<p>
18. NON-AGENCY. Nothing contained in this Agreement or the Dispute Policy shall be construed as creating any agency, partnership, or other form of joint enterprise between the parties.
</p>
<p>
19. NON-WAIVER. Our failure to require performance by you of any provision hereof shall not affect the full right to require such performance at any time thereafter; nor shall the waiver by us of a 
breach of any provision hereof be taken or held to be a waiver of the provision itself.
</p>
<p>
20. NOTICES. Any notice, direction or other communication given under this Agreement shall be in writing and given by sending it via e-mail or via regular mail. In the case of e-mail, valid notice 
shall only have been deemed to have been given when an electronic confirmation of delivery has been obtained by the sender. In the case of e-mail notification to us to <bean:message bundle="systemConfig" key="systemEmail.email"/> or, in the 
case of notice to you, at the e-mail address provided by you. Any e-mail communication shall be deemed to have been validly and effectively given on the date of such communication, if such date is a 
business day and such delivery was made prior to 4:00 p.m. GMT, otherwise it will be deemed to have been delivered on the next business day. In the case of regular mail notice, valid notice shall be 
deemed to have been validly and effectively given 5 business days after the date of mailing and, in the case of notification to us shall be sent to:
<br />
    Verein Doctor-Doc, D&auml;nikerstrasse 7, CH-4653 Oberg&ouml;sgen, Switzerland
<br />
and in the case of notification to you shall be to the address specified in your doctor-doc-Account.
</p>
<p>
21. ENTIRETY. You agree that this Agreement, the rules and policies published by us and the Dispute Policy are the complete and exclusive agreement between you and us regarding our Services. This 
Agreement and the Dispute Policy supersede all prior agreements and understandings, whether established by custom, practice, policy or precedent.
</p>
<p>
22. GOVERNING LAW. THIS AGREEMENT SHALL BE GOVERNED BY AND INTERPRETED AND ENFORCED IN ACCORDANCE WITH THE FEDERAL LAWS OF SWITZERLAND. ANY ACTION RELATING TO THIS AGREEMENT MUST BE BROUGHT IN OLTEN 
AND YOU IRREVOCABLY CONSENT TO THE JURISDICTION OF SUCH COURTS.
</p>
<p>
23. INFANCY. You attest that you are of legal age to enter into this Agreement.
</p>
<p>
24. ACCEPTANCE OF AGREEMENT. YOU ACKNOWLEDGE THAT YOU HAVE READ THIS AGREEMENT AND AGREE TO ALL ITS TERMS AND CONDITIONS. YOU HAVE INDEPENDENTLY EVALUATED THE DESIRABILITY OF THE SERVICE AND ARE NOT 
RELYING ON ANY REPRESENTATION AGREEMENT., GUARANTEE OR STATEMENT OTHER THAN AS SET FORTH IN THIS AGREEMENT.
</p>
<p>
Additional Terms and Conditions:
</p>
<p>
1. Client shall be the only authorized user of the Services under this Agreement. You shall be responsible for the confidentiality and use of your username and password. You understand that you shall 
be solely responsible for all transactions through the Services using your username and password. You further understand and agree that, as a condition of using the Services, you shall immediately 
notify us if you become aware of any unauthorized use of your username and/or password.
</p>
<p>
2. Please safeguard your username and password from any unauthorized use. You agree that in the event your username and/or password is transmitted to a third party through no action on the part of us, 
neither us nor any of our officers, employees, agents, affiliates or subsidiaries can or will have any responsibility or liability to you or to any other person whose claim may arise through you 
for any claims with respect to the handling, mishandling or loss of your username and/or password.
</p>
<p>
3. You agree that you will not chargeback any amounts previously charged to your credit card by us. If you chargeback a credit card charge for a payment initiated by you, you agree that we may 
recover the amount of the chargeback, as well as the chargeback fee currently set at $15.00 per chargeback by any means we deem necessary, including but not limited to re-charging your credit 
card for the chargeback. Furthermore, if you initiate a chargeback of a previous credit card charge, access to your account may be denied until payment is made and your account may be terminated, 
both at the sole discretion of us.
</p>
<p>
4. You will indemnify us and hold us harmless from any loss that may occur due to any loss of registration, access interruptions to our services, lost data between you and us or a third party, 
and lost profits due to indirect, incidental or consequential damages regardless of the form of action. In no event shall our liability exceed the total amount paid by you to us.
</p>
<p>
5. You agree to pay us all registration fees as shown on the Fee Schedule. We reserve the right to change the existing Fee Schedule at any time at our sole discretion. All fees are non-refundable. 
Your registration may, at our sole discretion, be canceled if any fees are unpaid on the date they become due.
</p>
<p>
6. You agree to defend, indemnify and hold harmless us and our directors, officers, employees and agents for any loss, damages or costs, including reasonable attorneys' fees, resulting from any 
third party claim, action, or demand related to your domain name or its use.
</p>
<p>
7. The Verein Doctor-Doc provides its Services to you on an "as-is," "as-available" basis. Neither Doctor-Doc, nor any of its directors, officers, employees or agents shall have any liability to 
you for any failure or delay to maintain or provide to you any Services offered by Doctor-Doc.

</p>


 <logic:present name="userinfo" property="benutzer.gtc">
 <logic:equal name="userinfo" property="benutzer.gtc" value="">
 <html:form action="gtc_" method="post">

<table>

 <logic:present name="orderform">
 <input type="hidden" name="resolver" value="true" />
 <input type="hidden" name="issn" value="<bean:write name="orderform" property="issn" />" />
 <input type="hidden" name="mediatype" value="<bean:write name="orderform" property="mediatype" />" />
 <input type="hidden" name="jahr" value="<bean:write name="orderform" property="jahr" />" />
 <input type="hidden" name="jahrgang" value="<bean:write name="orderform" property="jahrgang" />" />
 <input type="hidden" name="heft" value="<bean:write name="orderform" property="heft" />" />
 <input type="hidden" name="seiten" value="<bean:write name="orderform" property="seiten" />" />
 <input type="hidden" name="isbn" value="<bean:write name="orderform" property="isbn" />" />
 <input type="hidden" name="artikeltitel" value="<bean:write name="orderform" property="artikeltitel" />" />
 <input type="hidden" name="zeitschriftentitel" value="<bean:write name="orderform" property="zeitschriftentitel" />" />
 <input type="hidden" name="author" value="<bean:write name="orderform" property="author" />" />
 <input type="hidden" name="kapitel" value="<bean:write name="orderform" property="kapitel" />" />
 <input type="hidden" name="buchtitel" value="<bean:write name="orderform" property="buchtitel" />" />
 <input type="hidden" name="verlag" value="<bean:write name="orderform" property="verlag" />" />
 <input type="hidden" name="rfr_id" value="<bean:write name="orderform" property="rfr_id" />" />
 <input type="hidden" name="genre" value="<bean:write name="orderform" property="genre" />" />
 <input type="hidden" name="pmid" value="<bean:write name="orderform" property="pmid" />" />
 <input type="hidden" name="doi" value="<bean:write name="orderform" property="doi" />" />
 <input type="hidden" name="sici" value="<bean:write name="orderform" property="sici" />" />
 <input type="hidden" name="lccn" value="<bean:write name="orderform" property="lccn" />" />
 <input type="hidden" name="zdbid" value="<bean:write name="orderform" property="zdbid" />" />
 <input type="hidden" name="artikeltitel_encoded" value="<bean:write name="orderform" property="artikeltitel_encoded" />" />
 <input type="hidden" name="author_encoded" value="<bean:write name="orderform" property="author_encoded" />" />
 <input type="hidden" name="foruser" value="<bean:write name="orderform" property="foruser" />" />
 </logic:present>


	<tr>
		<td><html:submit property="method" value="accept" /></td>
		<td><br /></td>
		<td><html:submit property="method" value="decline" /></td>
	</tr>
</table>

 </html:form>
 </logic:equal>
 </logic:present>

</div>

 
 </body>
</html>
