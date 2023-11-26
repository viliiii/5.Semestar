package hr.fer.oprpp1.hw04.db;

import java.util.LinkedList;
import java.util.List;

/**
 *  Filter for whole query.
 */
public class QueryFilter implements IFilter{

    LinkedList<ConditionalExpression> queries;

    /**
     * Constructor. Saves List<ConditionalExpression> that was parsed from the query.
     * @param queries List of ConditionalExpressions.
     */
    public QueryFilter(LinkedList<ConditionalExpression> queries) {
        this.queries = queries;
    }

    /**
     * Checks if given StudentRecord passes all the conditions from
     * List<ConditionalExpression> queries given through the constructor.
     * @param record the StudentRecord
     * @return true if all conditions are satisfied for the given record, false otherwise.
     */
    @Override
    public boolean accepts(StudentRecord record) {
        for(ConditionalExpression condEx: queries){
            if(!condEx.isNegation()){
                if(!condEx.getComparisonOperator().satisfied(condEx.getFieldGetter().get(record), condEx.getStringLiteral())){
                    return false;
                }
            }else{
                if(condEx.getComparisonOperator().satisfied(condEx.getFieldGetter().get(record), condEx.getStringLiteral())){
                    return false;
                }
            }
        }
        return true;
    }
}
