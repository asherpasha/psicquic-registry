package org.hupo.psi.mi.psicquic.ols.client;

import org.springframework.web.client.RestClientException;
import uk.ac.ebi.pride.utilities.ols.web.service.config.OLSWsConfigProd;
import uk.ac.ebi.pride.utilities.ols.web.service.model.Identifier;
import uk.ac.ebi.pride.utilities.ols.web.service.model.Term;

import java.util.HashMap;
import java.util.Map;

public class OLSClient {

    private uk.ac.ebi.pride.utilities.ols.web.service.client.OLSClient olsClient;

    public OLSClient() {
        this.olsClient = new uk.ac.ebi.pride.utilities.ols.web.service.client.OLSClient(new OLSWsConfigProd());
    }


    /**
     * calls OLS webserver and gets child terms for a termId
     *
     * @param termAccession termAccessopm
     * @param ontology      ontology
     * @return Map of child terms - key is termId, value is termName
     * Map should not be null.
     */
    public Map<String, String> getTermChildren(String termAccession, String ontology) {

        final Map<String, String> metadata = new HashMap<String, String>();
        try {
            //OLS client is sensitive case if the ontology es MI instead of mi doesn't return any result.
            Identifier identifier = new Identifier(termAccession, Identifier.IdentifierType.OBO);
            for (Term term : olsClient.getTermChildren(identifier, ontology.toLowerCase(), 1)) {
                metadata.put(term.getTermOBOId().getIdentifier(), term.getLabel());
            }
        } catch (RestClientException e) {
            e.printStackTrace();
        }

        return metadata;
    }

    /**
     * calls OLS webserver and gets suggestions of terms for a given query
     *
     * @param text     text
     * @param ontology ontology
     * @return of suggested terms - key is termId, value is termName.
     * Map should not be null.
     */
    public Map<String, String> getTermsByName(String text, String ontology) {

        final Map<String, String> metadata = new HashMap<String, String>();
        try {
            //OLS client is sensitive case if the ontology es MI instead of mi doesn't return any result.
            Term term = olsClient.getExactTermByName(text, ontology.toLowerCase());
            if (term != null) {
                metadata.put(term.getLabel(), term.getTermOBOId().getIdentifier());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return metadata;

    }

    public String getTermNameByTermId(String termId, String ontology) {
        String retval = "";
        try {
            //OLS client is sensitive case if the ontology es MI instead of mi doesn't return any result.
            Identifier identifier = new Identifier(termId, Identifier.IdentifierType.OBO);
            retval = olsClient.getTermById(identifier, ontology.toLowerCase()).getLabel();
            if (retval == null) {
                retval = "";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return retval;

    }
}

